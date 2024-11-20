package com.app.fire.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.AccidentAdapter
import com.app.fire.adapter.ChatAdapter
import com.app.fire.databinding.FragmentListAccidentBinding
import com.app.fire.databinding.FragmentListChatBinding
import com.app.fire.model.ChatMessage
import com.app.fire.model.OrganizationModelFirestore

class ListChatFragment: Fragment() {
    companion object {
        val TAG = "homeMentor"
    }
    private lateinit var binding: FragmentListChatBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListChatBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val chatList = listOf(
            ChatMessage("Hey, how are you?", "Alice", "12:30 PM", false),
            ChatMessage("I'm good! What about you?", null, "12:31 PM", true),
            ChatMessage("Doing great, thanks for asking!", "Alice", "12:32 PM", false)
        )

        val adapterChat = ChatAdapter(chatList)
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterChat
        }

    }

    fun String?.checkIsEmptyOrNull(): Boolean {
        if (this == "null") {
            return true
        }
        if (isNullOrEmpty()) {
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
    }
}