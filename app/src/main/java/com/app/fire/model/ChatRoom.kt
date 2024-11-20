package com.app.fire.model

data class ChatRoom(
    val username: String,
    val lastMessage: String,
    val timestamp: String,
    val avatar: Int // Resource ID for avatar
)