package com.app.fire.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.ChatAdapter
import com.app.fire.databinding.FragmentListChatBinding
import com.app.fire.model.ChatMessage
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
    private var roomId: String? = null // Initially null for new room
    private lateinit var adapterChat: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()
    private lateinit var binding: FragmentListChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat Rooms"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Go back to the previous screen
        }
        adapterChat = ChatAdapter(messages)
        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@ListChatActivity)
            adapter = ChatAdapter(messages)
            adapter = adapterChat
        }
        roomId = intent.getStringExtra("roomId")

        // Handle Send Button Click
        binding.btnSend.setOnClickListener {
            val message = binding.etChatInput.text.toString().trim()
            if (message.isNotEmpty()) {
                if (SessionManager.getTypeUser(this) == 1) {
                    if (roomId == null) {
                        createNewRoomAndSaveMessage(message)
                    } else {
                        saveChatToFirestore(message)
                    }
                    binding.etChatInput.text?.clear()
                }
            } else {
                Toast.makeText(this, "Message cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
        getChatData()
    }

    private fun createNewRoomAndSaveMessage(firstMessage: String) {
        // Generate a new room ID
        val newRoomRef = firestore.collection("rooms").document()
        roomId = newRoomRef.id

        val newRoomData = mapOf(
            "username" to SessionManager.getName(this), // You can customize this
            "lastMessage" to firstMessage,
            "timestamp" to Timestamp.now().toDate().toString(),
            "senderId" to SessionManager.getId(this)
        )

        // Save the new room and the first message
        newRoomRef.set(newRoomData)
            .addOnSuccessListener {
                saveChatToFirestore(firstMessage)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to create room: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun saveChatToFirestore(message: String) {
        if (roomId == null) return // Safety check

        val chatData = ChatMessage(
            message = message,
            senderName = "Your Name", // Replace with the current user's name
            timestamp = Timestamp.now().toDate()
                .toString(), // Convert Firestore timestamp to String
            roomId = roomId,
            sent = SessionManager.getTypeUser(this) != 2,// Assume the user is the sender
            senderId = SessionManager.getId(this)
        )
        messages.add(chatData)
        adapterChat.notifyItemInserted(messages.size - 1)
        binding.rv.scrollToPosition(messages.size - 1)
        // Save the message to the "chats" subcollection
        FirestoreUtil.addDocument(
            collectionPath = "chatsList",
            data = chatData,
            onSuccess = {
                updateRoomLastMessage(message)
            },
            onFailure = { e ->
                Toast.makeText(this, "Failed to send message: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    private fun updateRoomLastMessage(lastMessage: String) {
        if (roomId == null) return // Safety check

        val roomUpdate = mapOf(
            "lastMessage" to lastMessage,
            "timestamp" to Timestamp.now()
        )

        // Update the room's last message and timestamp
        firestore.collection("rooms")
            .document(roomId!!)
            .update(roomUpdate)
            .addOnSuccessListener {
                Toast.makeText(this, "Message sent", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update room: ${e.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun getChatData() {
        FirestoreUtil.findRoomByCriteria(
            field = "senderId",
            value = SessionManager.getId(this), // Replace with the value to search for
            onSuccess = { roomIds ->
                if (roomIds.isNotEmpty()) {
                    roomIds.forEach { room ->
                        this.roomId = room
                        getListChat()
                    }
                } else {
                }
            },
            onFailure = { e ->
                Log.e("FirestoreError", "Failed to find rooms: ${e.message}")
            }
        )
    }

    private fun getListChat() {
        showLoading("")
        FirebaseFirestore.getInstance().collection("chatsList")
            .whereEqualTo("roomId", roomId)
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot?> ->
                if (task.isSuccessful) {
                    val document = task.result
                    document?.forEach {
                        messages.add(
                            ChatMessage(
                                message = it.data["message"].toString(),
                                timestamp = it.data["timestamp"].toString(),
                                sent = it.data["senderId"].toString() == SessionManager.getId(this)
                            )
                        )
                        adapterChat.notifyItemInserted(messages.size - 1)
                        binding.rv.scrollToPosition(messages.size - 1)
                    }
                    hideLoading()
                }
            }
    }
}