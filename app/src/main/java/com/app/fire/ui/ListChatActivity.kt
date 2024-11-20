package com.app.fire.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.ChatAdapter
import com.app.fire.databinding.ActivityHomeBinding
import com.app.fire.databinding.FragmentListChatBinding
import com.app.fire.model.ChatMessage
import com.app.fire.util.BaseView

class ListChatActivity : BaseView() {
    private lateinit var binding: FragmentListChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val chatList = listOf(
            ChatMessage("Hey, how are you?", "Alice", "12:30 PM", false),
            ChatMessage("I'm good! What about you?", null, "12:31 PM", true),
            ChatMessage("Doing great, thanks for asking!", "Alice", "12:32 PM", false)
        )
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Chat Rooms"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Go back to the previous screen
        }
        val adapterChat = ChatAdapter(chatList)
        binding.rv.apply {
            layoutManager = LinearLayoutManager(this@ListChatActivity)
            adapter = adapterChat
        }
    }
}