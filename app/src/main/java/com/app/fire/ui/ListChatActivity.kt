package com.app.fire.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.ChatAdapter
import com.app.fire.databinding.FragmentListChatBinding
import com.app.fire.model.ChatMessage
import com.app.fire.model.NotificationItem
import com.app.fire.util.BaseView
import com.app.fire.util.FirestoreUtil
import com.app.fire.util.SessionManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class ListChatActivity : BaseView() {
    private val firestore = FirebaseFirestore.getInstance()
    private var roomId: String? = null
    private lateinit var binding: FragmentListChatBinding
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var adapterChat: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupSendButton()
        roomId = intent.extras?.getString("roomId", "")
        if (SessionManager.getTypeUser(this) == 2) {
            fetchChatMessages()
        } else {
            loadChatData()
        }
    }

    private fun setupUI() {
        binding = FragmentListChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Chat Rooms"
        }

        adapterChat = ChatAdapter()
        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@ListChatActivity)
            adapter = adapterChat
        }
    }

    private fun setupSendButton() {
        binding.btnSend.setOnClickListener {
            val message = binding.etChatInput.text.toString().trim()
            if (message.isNotEmpty()) {
                sendMessage(message)
                binding.etChatInput.text?.clear()
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendMessage(message: String) {
        if (roomId == null) {
            if (SessionManager.getTypeUser(this) == 1) {
                createNewRoomAndSendMessage(message)
            }
        } else {
            saveChatMessage(message)
        }
    }

    private fun createNewRoomAndSendMessage(firstMessage: String) {
        val newRoomRef = firestore.collection("rooms").document()
        roomId = newRoomRef.id

        val newRoomData = mapOf(
            "username" to SessionManager.getName(this),
            "lastMessage" to firstMessage,
            "timestamp" to Timestamp.now().toDate().toString(),
            "senderId" to SessionManager.getId(this)
        )

        newRoomRef.set(newRoomData)
            .addOnSuccessListener { saveChatMessage(firstMessage) }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to create room: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun saveChatMessage(message: String) {
        val chatMessage = ChatMessage(
            message = message,
            senderName = SessionManager.getName(this),
            timestamp = Timestamp.now().toDate().toString(),
            roomId = roomId,
            sent = true,
            senderId = SessionManager.getId(this),
            time = System.currentTimeMillis()
        )

        FirestoreUtil.addDocument(
            collectionPath = "chatsList",
            data = chatMessage,
            onSuccess = {
                updateRoomLastMessage(message)
                updateUIWithNewMessage(chatMessage)
                saveNotification()
            },
            onFailure = { e ->
                Toast.makeText(this, "Failed to send message: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    private fun updateUIWithNewMessage(chatMessage: ChatMessage) {
        adapterChat.addData(chatMessage)
        binding.rv.scrollToPosition(adapterChat.itemCount - 1)
    }

    private fun updateRoomLastMessage(lastMessage: String) {
        roomId?.let { id ->
            val roomUpdate = mapOf(
                "lastMessage" to lastMessage,
                "timestamp" to Timestamp.now().toDate().toString(),
            )

            firestore.collection("rooms")
                .document(id)
                .update(roomUpdate)
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to update room: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }

    private fun loadChatData() {
        FirestoreUtil.findRoomByCriteria(
            field = "senderId",
            value = SessionManager.getId(this),
            onSuccess = { roomIds ->
                roomIds.firstOrNull()?.let { id ->
                    roomId = id
                    fetchChatMessages()
                }
            },
            onFailure = { e ->
                Log.e("FirestoreError", "Failed to find rooms: ${e.message}")
            }
        )
    }

    private fun fetchChatMessages() {
        roomId?.let { id ->
            showLoading("")
            FirebaseFirestore.getInstance().collection("chatsList")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .whereEqualTo("roomId", id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result?.forEach { document ->
                            Log.e(
                                "fetchChatMessages",
                                "${document.data["message"].toString()}test: ${document.data["senderId"].toString()} - ${
                                    SessionManager.getId(this)
                                }"
                            )
                            val chatMessage = ChatMessage(
                                message = document.data["message"].toString(),
                                senderName = document.data["senderName"].toString(),
                                timestamp = document.data["timestamp"].toString(),
                                sent = document.data["senderId"].toString() == SessionManager.getId(
                                    this
                                ),
                                time = document.data["time"].toString().toLong()
                            )
                            messages.add(chatMessage)
                        }
                    }
                    val sortedBy =messages.sortedBy { it.time }
                    sortedBy.forEach {chatMessage ->
                        Log.d("sortedBy ", " ${chatMessage.message}")
                        adapterChat.addData(chatMessage)
                    }
                    binding.rv.scrollToPosition(adapterChat.itemCount - 1)
                    hideLoading()
                }
        }

    }

    private fun saveNotification() {
        val chatMessage = NotificationItem(
            title = "Notification Chat",
            message = "Kamu mendapat chat baru dari ${SessionManager.getName(this)}",
            timestamp = Timestamp.now().toDate().toString(),
            roomId = roomId ?: "",
            senderId = SessionManager.getId(this),
            time = System.currentTimeMillis()
        )

        FirestoreUtil.addDocument(
            collectionPath = "notifications",
            data = chatMessage,
            onSuccess = {},
            onFailure = { e -> }
        )
    }
}