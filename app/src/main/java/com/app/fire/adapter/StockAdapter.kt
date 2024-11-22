package com.app.fire.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.R
import com.app.fire.model.AccidentModelFirestore
import com.app.fire.model.StockItem

class StockAdapter() :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {
    private val stocks = ArrayList<StockItem>()
    fun addAll(organizations: List<StockItem>) {
        this.stocks.clear()
        this.stocks.addAll(organizations)
        notifyDataSetChanged()
    }
    inner class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.imgItemIcon)
        val itemName: TextView = view.findViewById(R.id.tvItemName)
        val quantity: TextView = view.findViewById(R.id.tvQuantity)
        val distributionDate: TextView = view.findViewById(R.id.tvDistributionDate)
        val recipient: TextView = view.findViewById(R.id.tvRecipient)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_stock_distribution, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stockItem = stocks[position]
        holder.itemName.text = stockItem.itemName
        holder.quantity.text = stockItem.quantity
        holder.distributionDate.text = stockItem.distributionDate
        holder.recipient.visibility=View.GONE
        holder.icon.setImageResource(stockItem.icon)
    }

    override fun getItemCount(): Int = stocks.size
}