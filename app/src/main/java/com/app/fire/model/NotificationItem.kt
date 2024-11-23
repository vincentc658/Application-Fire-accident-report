package com.app.fire.model

import com.app.fire.util.BaseView

data class NotificationItem(
    val title: String,
    val message: String,
    val timestamp: String,
    val icon: Int=0, // Resource ID for notification icon
    val roomId : String="",
    val senderId : String="",
    val time: Long=0L,
    val isRead: Boolean=false,
) : BaseModel()