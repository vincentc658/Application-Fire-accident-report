package com.app.fire.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.R
import com.app.fire.databinding.ItemFireAccidentBinding
import com.app.fire.databinding.ItemStockDistributionBinding
import com.app.fire.model.AccidentModelFirestore
import com.app.fire.model.StockItem
import com.app.fire.util.BaseView
import com.app.fire.util.BaseView.Companion.KK
import com.app.fire.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

class StockAdapter(private var context: Context?= null, private var isShowCountItem : Boolean= false,
                   private var isShowDelete : Boolean=false,
                   private val onClick:(stock:StockItem)->Unit) :
    RecyclerView.Adapter<StockAdapter.StockViewHolder>() {
    private val stocks = ArrayList<StockItem>()
    fun addAll(organizations: List<StockItem>) {
        this.stocks.clear()
        this.stocks.addAll(organizations)
        notifyDataSetChanged()
    }
    fun add(organizations: StockItem) {
        this.stocks.add(organizations)
        notifyDataSetChanged()
    }
    fun getData()= stocks
    fun replaceData(organizations: StockItem, position: Int){
        stocks[position]=organizations
        notifyItemChanged(position)
    }
    inner class StockViewHolder( val binding: ItemStockDistributionBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding =
            ItemStockDistributionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stockItem = stocks[position]
        holder.binding.tvItemName.text = stockItem.itemName
        holder.binding.tvQuantity.text = stockItem.quantity.toString()
        holder.binding.itemQuantity.text = stockItem.quantityPlan.toString()
        holder.binding.tvDistributionDate.text = stockItem.distributionDate
        holder.binding.tvRecipient.visibility=View.GONE
        holder.binding.imgItemIcon.setImageResource(stockItem.icon)
        if(isShowCountItem){
            holder.binding.llCountItem.visibility= View.VISIBLE
        }else{
            holder.binding.llCountItem.visibility= View.GONE
        }
        holder.itemView.setOnClickListener {
            onClick(stockItem)
        }
        holder.binding.decreaseButton.setOnClickListener {
            if(stockItem.quantityPlan>0) {
                var quantity= stocks[position].quantityPlan
                quantity-=1
                stocks[position].quantityPlan=quantity
                notifyItemChanged(position)
            }
        }
        holder.binding.increaseButton.setOnClickListener {
            var quantity= stocks[position].quantityPlan
            quantity+=1
            stocks[position].quantityPlan=quantity
            notifyItemChanged(position)
        }
        holder.binding.deleteButton.setOnClickListener {
            stocks.removeAt(position)
            notifyItemRemoved(position)
        }
        if(isShowDelete){
            if(SessionManager.getTypeUser(context)==2){
                holder.binding.ivDelete.visibility= View.VISIBLE
            }else{
                holder.binding.ivDelete.visibility= View.GONE
            }
            holder.binding.ivDelete.setOnClickListener {
                (context as BaseView).showLoading("")
                FirebaseFirestore.getInstance().collection("stockLogistic").document(stockItem.id).delete()
                    .addOnSuccessListener {
                        stocks.removeAt(holder.layoutPosition)
                        notifyItemRemoved(holder.layoutPosition)
                        (context as BaseView).hideLoading()
                    }
            }
        }

    }

    override fun getItemCount(): Int = stocks.size
}