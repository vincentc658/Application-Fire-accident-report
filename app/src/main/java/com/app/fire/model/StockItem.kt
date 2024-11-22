package com.app.fire.model

import android.os.Parcelable
import com.app.fire.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockItem(
    var itemName: String="",
    var quantity: Int=0,
    var quantityPlan: Int=0,
    var distributionDate: String="",
    var recipient: String="",
    var icon: Int= R.drawable.ic_inbox_24 // Resource ID for item icon
) : BaseModel(), Parcelable{

}