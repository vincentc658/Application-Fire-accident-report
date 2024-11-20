package com.app.fire.model

data class StockItem(
    val itemName: String,
    val quantity: String,
    val distributionDate: String,
    val recipient: String,
    val icon: Int // Resource ID for item icon
)