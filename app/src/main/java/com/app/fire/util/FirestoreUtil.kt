package com.app.fire.util


import com.app.fire.model.BaseModel
import com.google.firebase.firestore.*

object FirestoreUtil {
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    /**
     * Add or update a document in a collection.
     */
    fun <T : Any> addOrUpdateDocument(
        collectionPath: String,
        documentId: String,
        data: T,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection(collectionPath)
            .document(documentId)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Add a document with auto-generated ID.
     */
    fun <T : Any> addDocument(
        collectionPath: String,
        data: T,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection(collectionPath)
            .add(data)
            .addOnSuccessListener { documentRef -> onSuccess(documentRef.id) }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Get a document by its ID.
     */
    fun <T> getDocument(
        collectionPath: String,
        documentId: String,
        clazz: Class<T>,
        onSuccess: (T?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection(collectionPath)
            .document(documentId)
            .get()
            .addOnSuccessListener { snapshot ->
                val data = if (snapshot.exists()) snapshot.toObject(clazz) else null
                onSuccess(data)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Get all documents in a collection.
     */
    fun <T> getAllDocuments(
        collectionPath: String,
        clazz: Class<T>,
        onSuccess: (List<T>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection(collectionPath)
            .get()
            .addOnSuccessListener { snapshot ->
                val dataList = snapshot.documents.mapNotNull { document ->
                    try {
                        val data = document.toObject(clazz)
                        data?.apply {
                            if (this is BaseModel) this.id = document.id
                        }
                        data
                    } catch (e: Exception) {
                        null // Skip any documents that fail to parse
                    }
                }
                onSuccess(dataList)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    /**
     * Delete a document by its ID.
     */
    fun deleteDocument(
        collectionPath: String,
        documentId: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        firestore.collection(collectionPath)
            .document(documentId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Listen to real-time updates for a document.
     */
    fun <T> listenToDocument(
        collectionPath: String,
        documentId: String,
        clazz: Class<T>,
        onSuccess: (T?) -> Unit,
        onFailure: (Exception) -> Unit
    ): ListenerRegistration {
        return firestore.collection(collectionPath)
            .document(documentId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onFailure(e)
                    return@addSnapshotListener
                }
                val data =
                    if (snapshot != null && snapshot.exists()) snapshot.toObject(clazz) else null
                onSuccess(data)
            }
    }

    /**
     * Listen to real-time updates for a collection.
     */
    fun <T> listenToCollection(
        collectionPath: String,
        clazz: Class<T>,
        onSuccess: (List<T>) -> Unit,
        onFailure: (Exception) -> Unit
    ): ListenerRegistration {
        return firestore.collection(collectionPath)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    onFailure(e)
                    return@addSnapshotListener
                }
                val dataList = if (snapshot != null) snapshot.toObjects(clazz) else emptyList()
                onSuccess(dataList)
            }
    }
}
