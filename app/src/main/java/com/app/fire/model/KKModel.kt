package com.app.fire.model

data class KKModel(
    var namaKK: String = "",
    var time: Long = 0L,
    var data: ArrayList<StockItem> = ArrayList()
) : BaseModel()