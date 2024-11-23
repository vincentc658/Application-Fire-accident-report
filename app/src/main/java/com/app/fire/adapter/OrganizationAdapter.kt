package com.app.fire.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.databinding.ItemOrganizationBinding
import com.app.fire.model.OrganizationModelFirestore

class OrganizationAdapter(private val context: Context) :
    RecyclerView.Adapter<OrganizationAdapter.MovieViewHolder>() {

    private val organizations = mutableListOf<OrganizationModelFirestore>()

    fun addAll(organizations: List<OrganizationModelFirestore>) {
        this.organizations.clear()
        this.organizations.addAll(organizations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemOrganizationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(organizations[position])
    }

    override fun getItemCount(): Int = organizations.size

    inner class MovieViewHolder(private val binding: ItemOrganizationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: OrganizationModelFirestore) {
            binding.tvOrganizationEmail.text = movie.email
            binding.tvOrganizationName.text = movie.name
            binding.tvOrganizationNumber.text =
                movie.phoneNumber.chunked(4).joinToString(separator = " ")
            itemView.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://wa.me/${movie.phoneNumber}")
                }
                context.startActivity(intent)
            }
        }
    }
}