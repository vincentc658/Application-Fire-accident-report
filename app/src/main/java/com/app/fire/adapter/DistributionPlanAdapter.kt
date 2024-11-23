package com.app.fire.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.databinding.ItemDistributionPlanBinding
import com.app.fire.databinding.ItemFireAccidentBinding
import com.app.fire.model.AccidentModelFirestore
import com.app.fire.model.LogisticPlan
import kotlin.math.log

class DistributionPlanAdapter(private val context: Context) :
    RecyclerView.Adapter<DistributionPlanAdapter.MovieViewHolder>() {

    private val organizations = ArrayList<LogisticPlan>()

    fun addAll(organizations: List<LogisticPlan>) {
        this.organizations.clear()
        this.organizations.addAll(organizations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemDistributionPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(organizations[position])
    }

    override fun getItemCount(): Int = organizations.size

    inner class MovieViewHolder(private val binding: ItemDistributionPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: LogisticPlan) {
            binding.tvLocation.text = "🔥 Lokasi: ${movie.location}"
            binding.tvTime.text = "🕒 Waktu: ${movie.timestamp}"
            var logistic =""
            movie.data.forEachIndexed { index, stockItem ->
                logistic="$logistic${index+1}. ${stockItem.itemName} : ${stockItem.quantity}\n"
            }
            binding.tvLogistic.text= logistic
        }
    }
}