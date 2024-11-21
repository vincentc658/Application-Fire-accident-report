package com.app.fire.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.R
import com.app.fire.model.ChatMessage
import java.text.SimpleDateFormat
import java.util.Locale


class ChatAdapter() :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    private val chatList: ArrayList<ChatMessage> = ArrayList()

    inner class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val chatBubble: LinearLayout = view.findViewById(R.id.chatBubble)
        val senderName: TextView = view.findViewById(R.id.tvSenderName)
        val messageText: TextView = view.findViewById(R.id.tvMessage)
        val timestamp: TextView = view.findViewById(R.id.tvTimestamp)
        val profileImage: ImageView = view.findViewById(R.id.imgProfile)
    }
    fun addData(chat :ChatMessage){
        chatList.add(chat)
        notifyItemInserted(chatList.size-1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chatMessage = chatList[position]

        // Set message text and timestamp
        holder.messageText.text = chatMessage.message
        holder.timestamp.text = formatDateTime(chatMessage.timestamp)

        // Show/hide sender name and profile for received messages
        if (!chatMessage.sent) {
            holder.senderName.visibility = View.VISIBLE
            holder.senderName.text = chatMessage.senderName
            holder.profileImage.visibility = View.VISIBLE

            // Position chat bubble for received message
            holder.chatBubble.background.state = intArrayOf()
            (holder.chatBubble.layoutParams as RelativeLayout.LayoutParams).apply {
                removeRule(RelativeLayout.ALIGN_PARENT_END)
                addRule(RelativeLayout.ALIGN_PARENT_START)
            }
        } else {
            holder.senderName.visibility = View.GONE
            holder.profileImage.visibility = View.GONE

            // Position chat bubble for sent message
            holder.chatBubble.background.state = intArrayOf(android.R.attr.state_selected)
            (holder.chatBubble.layoutParams as RelativeLayout.LayoutParams).apply {
                removeRule(RelativeLayout.ALIGN_PARENT_START)
                addRule(RelativeLayout.ALIGN_PARENT_END)
            }
        }
    }

    override fun getItemCount(): Int = chatList.size
    private fun formatDateTime(input: String): String {
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
