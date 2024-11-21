package com.app.fire.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.R
import com.app.fire.model.ChatRoom
import java.text.SimpleDateFormat
import java.util.Locale

class ChatRoomAdapter(
    private val chatList: List<ChatRoom>,
    private val onClick: (roomChat: ChatRoom) -> Unit
) :
    RecyclerView.Adapter<ChatRoomAdapter.ChatRoomViewHolder>() {

    inner class ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.imgAvatar)
        val username: TextView = view.findViewById(R.id.tvUsername)
        val lastMessage: TextView = view.findViewById(R.id.tvLastMessage)
        val timestamp: TextView = view.findViewById(R.id.tvTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_chat_room, parent, false)
        return ChatRoomViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
        val chatRoom = chatList[position]

        holder.username.text = chatRoom.senderId
        holder.username.text = chatRoom.senderId
        holder.lastMessage.text = chatRoom.lastMessage
        holder.timestamp.text = formatDateTime(chatRoom.timestamp)
        holder.itemView.setOnClickListener {
            onClick(chatRoom)
        }
//        holder.avatar.setImageResource(chatRoom.avatar)
    }

    override fun getItemCount(): Int = chatList.size
    fun formatDateTime(input: String): String {
        val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val outputFormat = SimpleDateFormat("EEE MMM dd HH:mm", Locale.ENGLISH)
        return try {
            val date = inputFormat.parse(input) // Parse the input date
            outputFormat.format(date!!) // Format it to the desired output
        } catch (e: Exception) {
            "Invalid date" // Handle parsing errors
        }
    }
}
