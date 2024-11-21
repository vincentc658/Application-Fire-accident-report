package com.app.fire.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.R
import com.app.fire.model.NotificationItem
import java.text.SimpleDateFormat
import java.util.Locale


class NotificationAdapter(private val notifications: List<NotificationItem>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.imgNotificationIcon)
        val title: TextView = view.findViewById(R.id.tvNotificationTitle)
        val message: TextView = view.findViewById(R.id.tvNotificationMessage)
        val timestamp: TextView = view.findViewById(R.id.tvNotificationTimestamp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.icon.setImageResource(notification.icon)
        holder.title.text = notification.title
        holder.message.text = notification.message
        holder.timestamp.text = formatDateTime(notification.timestamp)
    }

    override fun getItemCount(): Int = notifications.size
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
