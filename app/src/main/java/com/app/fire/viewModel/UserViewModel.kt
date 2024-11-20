package com.app.fire.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.fire.model.UserModelFirestore
import com.app.fire.util.FirestoreUtil
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    private val _userLiveData = MutableLiveData<UserModelFirestore?>()
    val userLiveData: LiveData<UserModelFirestore?> get() = _userLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    /**
     * Register user to Firestore.
     */
    fun registerToFirestore(
        userId: String,
        userData: UserModelFirestore,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        viewModelScope.launch {
            FirestoreUtil.addOrUpdateDocument(
                collectionPath = "users",
                documentId = userId,
                data = userData,
                onSuccess = { onSuccess() },
                onFailure = { e -> onFailure(e) }
            )
        }
    }
    fun fetchUser(userId: String) {
        FirestoreUtil.getDocument(
            collectionPath = "users",
            documentId = userId,
            clazz = UserModelFirestore::class.java,
            onSuccess = { user ->
                _userLiveData.value = user
            },
            onFailure = { exception ->
                _errorLiveData.value = exception.message
            }
        )
    }
}
