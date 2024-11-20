package com.app.fire.model

data class NotificationItem(
    val title: String,
    val message: String,
    val timestamp: String,
    val icon: Int // Resource ID for notification icon
)