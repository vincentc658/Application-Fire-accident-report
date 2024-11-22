package com.app.fire.model

import com.app.fire.R

data class StockItem(
    val itemName: String,
    val quantity: String,
    val distributionDate: String,
    val recipient: String="",
    val icon: Int= R.drawable.ic_inbox_24 // Resource ID for item icon
)