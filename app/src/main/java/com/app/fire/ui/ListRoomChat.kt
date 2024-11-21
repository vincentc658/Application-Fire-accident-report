package com.app.fire.ui

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.ChatRoomAdapter
import com.app.fire.databinding.ActivityListRoomBinding
import com.app.fire.model.ChatMessage
import com.app.fire.model.ChatRoom
import com.app.fire.model.OrganizationModelFirestore
import com.app.fire.util.BaseView
import com.app.fire.util.FirestoreUtil

class ListRoomChat : BaseView() {
    private lateinit var binding: ActivityListRoomBinding
    private val roomsData = mutableListOf<ChatRoom>()
    private lateinit var adapterChat: ChatRoomAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat Rooms"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Go back to the previous screen
        }
        adapterChat = ChatRoomAdapter(roomsData) { chatRoom ->
            val bundle = Bundle()
            bundle.putString("roomId", chatRoom.id)
            goToPage(ListChatActivity::class.java, bundle)
        }

        binding.recyclerViewChats.apply {
            layoutManager = LinearLayoutManager(this@ListRoomChat)
            adapter = adapterChat
        }
        getRoomChat()
    }

    private fun getRoomChat() {
        FirestoreUtil.getAllDocuments(
            collectionPath = "rooms",
            clazz = ChatRoom::class.java,
            onSuccess = { rooms ->
                roomsData.addAll(rooms)
                adapterChat.notifyDataSetChanged()
            },
            onFailure = { exception ->
            }
        )
    }
}