package com.app.fire.model

data class ChatRoom(
    val username: String="",
    val senderId: String="",
    val lastMessage: String="",
    val timestamp: String="",
) : BaseModel()