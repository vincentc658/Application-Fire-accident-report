package com.app.fire.ui

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.R
import com.app.fire.adapter.NotificationAdapter
import com.app.fire.databinding.ActivityNotificationBinding
import com.app.fire.model.NotificationItem
import com.app.fire.util.BaseView
import com.app.fire.util.FirestoreUtil
import com.app.fire.util.SessionManager
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore

class NotificationActivity : BaseView() {
    private lateinit var notificationAdapter: NotificationAdapter
    private val notifications = mutableListOf<NotificationItem>()
    private lateinit var binding: ActivityNotificationBinding
    private var roomId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        notificationAdapter = NotificationAdapter(this) {
            val bundle = Bundle()
            bundle.putString("roomId", it.roomId)
            goToPage(ListChatActivity::class.java, bundle)
            updateStatus(it.id)
        }

        binding.recyclerViewNotifications.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewNotifications.adapter = notificationAdapter
    }

    private fun getNotification() {
        FirebaseFirestore.getInstance().collection("notifications")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.forEach { document ->
                        val senderId = document.data["senderId"].toString()
                        val roomId = document.data["roomId"].toString()
                        if (senderId != SessionManager.getId(this)) {
                            val notificationItem = NotificationItem(
                                message = document.data["title"].toString(),
                                title = document.data["message"].toString(),
                                timestamp = document.data["timestamp"].toString(),
                                roomId = document.data["roomId"].toString(),
                                senderId = document.data["senderId"].toString(),
                                isRead = (document.data["isRead"] ?: "false").toString()
                                    .toBoolean(),
                                icon = R.drawable.ic_notifications
                            )
                            notificationItem.id = document.id
                            if (SessionManager.getTypeUser(this) == 2) { // admin
                                notifications.add(notificationItem)
                            } else {
                                if (this.roomId == roomId) {
                                    notifications.add(notificationItem)
                                }
                            }
                        }
                    }
                    notificationAdapter.addAll(notifications)
                    binding.recyclerViewNotifications.scrollToPosition(notifications.size - 1)
                }
            }
    }

    private fun loadChatData() {
        notifications.clear()
        if (SessionManager.getTypeUser(this) == 2) { // admin
            getNotification()
            return
        }
        FirestoreUtil.findRoomByCriteria(
            field = "senderId",
            value = SessionManager.getId(this),
            onSuccess = { roomIds ->
                roomIds.firstOrNull()?.let { id ->
                    roomId = id
                    getNotification()
                }
            },
            onFailure = { e ->
                Log.e("FirestoreError", "Failed to find rooms: ${e.message}")
            }
        )
    }

    private fun updateStatus(id: String) {
        val dataNotification = HashMap<String, Any>()
        dataNotification["isRead"] = true
        FirebaseFirestore.getInstance().collection(NOTIFICATIONS).document(id)
            .update(dataNotification)
    }

    override fun onResume() {
        super.onResume()
        loadChatData()
    }
}
