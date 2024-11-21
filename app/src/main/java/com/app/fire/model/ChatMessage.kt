package com.app.fire.model

data class ChatMessage(
    val message: String="",
    val senderName: String? = null,
    val senderId: String? = null,
    val roomId: String? = null,
    val timestamp: String="",
    val sent: Boolean=false,
    val time: Long=0L,
) : BaseModel()