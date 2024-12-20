package com.app.fire.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.fire.databinding.ItemDistributionPlanBinding
import com.app.fire.databinding.ItemKkBinding
import com.app.fire.model.KKModel
import com.app.fire.model.LogisticPlan
import com.app.fire.util.BaseView
import com.app.fire.util.BaseView.Companion.KK
import com.app.fire.util.BaseView.Companion.LOGISTIC_PLAN
import com.app.fire.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

class KKAdapter(private val context: Context) :
    RecyclerView.Adapter<KKAdapter.MovieViewHolder>() {

    private val organizations = ArrayList<KKModel>()

    fun addAll(organizations: List<KKModel>) {
        this.organizations.clear()
        this.organizations.addAll(organizations)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            ItemKkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(organizations[position])
    }

    override fun getItemCount(): Int = organizations.size

    inner class MovieViewHolder(private val binding: ItemKkBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: KKModel) {
            binding.tvKK.text = "Nama KK : ${movie.namaKK}"
            var logistic =""
            movie.data.forEachIndexed { index, stockItem ->
                logistic="$logistic${index+1}. ${stockItem.itemName} : ${stockItem.quantity}\n"
            }
            binding.tvLogistic.text= logistic
            if(SessionManager.getTypeUser(context)==2){
                binding.ivDelete.visibility= View.VISIBLE
            }else{
                binding.ivDelete.visibility= View.GONE
            }
            binding.ivDelete.setOnClickListener {
                (context as BaseView).showLoading("")
                FirebaseFirestore.getInstance().collection(KK).document(movie.id).delete()
                    .addOnSuccessListener {
                        organizations.removeAt(layoutPosition)
                        notifyItemRemoved(layoutPosition)
                        (context as BaseView).hideLoading()
                    }
            }
        }
    }
}