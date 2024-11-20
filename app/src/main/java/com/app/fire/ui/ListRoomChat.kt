package com.app.fire.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.ChatRoomAdapter
import com.app.fire.databinding.ActivityListRoomBinding
import com.app.fire.model.ChatRoom
import com.app.fire.util.BaseView

class ListRoomChat : BaseView() {
    private lateinit var binding: ActivityListRoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val chatList = listOf(
            ChatRoom("John Doe", "Hey, how are you?", "12:30 PM"),
            ChatRoom("Jane Smith", "See you tomorrow!", "11:45 AM"),
            ChatRoom("Mike Johnson", "Meeting at 3 PM?", "10:15 AM")
        )
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat Rooms"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Go back to the previous screen
        }

        val adapterChat = ChatRoomAdapter(chatList) { chatRoom ->
            goToPage(ListChatActivity::class.java)
        }
        binding.recyclerViewChats.apply {
            layoutManager = LinearLayoutManager(this@ListRoomChat)
            adapter = adapterChat
        }
    }
}