package com.app.fire.model

data class ChatMessage(
    val message: String,
    val senderName: String? = null,
    val timestamp: String,
    val isSent: Boolean // true if sent by the user, false if received
)