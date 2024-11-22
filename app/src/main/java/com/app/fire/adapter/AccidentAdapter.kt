package com.app.fire.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.databinding.ItemFireAccidentBinding
import com.app.fire.model.AccidentModelFirestore

class AccidentAdapter(private val context: Context) :
    RecyclerView.Adapter<AccidentAdapter.MovieViewHolder>() {

    private val organizations = ArrayList<AccidentModelFirestore>()

    fun addAll(organizations: List<AccidentModelFirestore>) {
        this.organizations.clear()
        this.organizations.addAll(organizations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemFireAccidentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(organizations[position])
    }

    override fun getItemCount(): Int = organizations.size

    inner class MovieViewHolder(private val binding: ItemFireAccidentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: AccidentModelFirestore) {
            binding.tvLocation.text = "🔥 Lokasi: ${movie.lokasi}"
            binding.tvTime.text = "🕒 Waktu: ${movie.waktu}"
            binding.tvDamagedHousesValue.text = movie.rumahRusak.toString()
            binding.tvVictimsAffectedValue.text = movie.korbanTerdampak.toString()
            binding.tvHouseholdsValue.text = movie.rumahRusak.toString()
        }
    }
}