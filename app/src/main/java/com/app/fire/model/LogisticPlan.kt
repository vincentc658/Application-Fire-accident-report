package com.app.fire.model

data class LogisticPlan(
    var location: String = "",
    var accidentId: String = "",
    var time: Long = 0L,
    var timestamp: String = "",
    var data: ArrayList<StockItem> = ArrayList()
) : BaseModel()