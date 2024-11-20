package com.app.fire.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.R
import com.app.fire.adapter.NotificationAdapter
import com.app.fire.model.NotificationItem

class NotificationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        // Set up Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Sample Data
        val notifications = listOf(
            NotificationItem("New Update", "Your profile has been updated successfully.", "2 hrs ago", R.drawable.ic_notifications),
            NotificationItem("Reminder", "Your subscription expires in 3 days.", "5 hrs ago", R.drawable.ic_notifications),
            NotificationItem("Message", "You received a new message from John.", "1 day ago", R.drawable.ic_notifications)
        )

        // Set up RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewNotifications)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = NotificationAdapter(notifications)
    }
}
