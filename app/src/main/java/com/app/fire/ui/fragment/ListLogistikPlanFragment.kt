package com.app.fire.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.AccidentAdapter
import com.app.fire.adapter.DistributionPlanAdapter
import com.app.fire.databinding.FragmentListOrganizationBinding
import com.app.fire.model.AccidentModelFirestore
import com.app.fire.model.LogisticPlan
import com.app.fire.model.StockItem
import com.app.fire.ui.AddLogisticActivity
import com.app.fire.ui.StockActivityInputActivity
import com.app.fire.util.BaseView
import com.app.fire.util.BaseView.Companion.LOGISTIC_PLAN
import com.app.fire.viewModel.OrganizationViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ListLogistikPlanFragment : Fragment() {
    companion object {
        val TAG = "homeMentor"
    }
    private lateinit var distributionPlanAdapter: DistributionPlanAdapter

    private lateinit var binding: FragmentListOrganizationBinding

    val distributionPlan = ArrayList<LogisticPlan>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListOrganizationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        distributionPlanAdapter = DistributionPlanAdapter(requireContext())
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = distributionPlanAdapter
        }
        binding.btnAdd.setOnClickListener {
            (requireActivity() as BaseView).goToPage(AddLogisticActivity::class.java)
        }
        lifecycleScope.launch {
            (requireActivity() as BaseView).showLoading("")
            val plans = getAllLogisticPlan2()
            distributionPlanAdapter.addAll(plans)
            (requireActivity() as BaseView).hideLoading()
        }
    }

    suspend fun getAllLogisticPlan2(): List<LogisticPlan> {
        val distributionPlan = ArrayList<LogisticPlan>()

        val documents = FirebaseFirestore.getInstance().collection(LOGISTIC_PLAN)
            .get().await() // Use Kotlin Coroutines' await() for Firestore tasks

        for (document in documents) {
            val documentId = document.id
            val logisticPlan = LogisticPlan(
                location = document.data["location"].toString(),
                accidentId = document.data["accidentId"].toString(),
                timestamp = "${document.data["timestamp"].toString()} / ${document.data["jam"].toString().toValidString()}",
                time = document.data["time"].toString().toLong()
            )
            logisticPlan.id= documentId

            val subDocuments = FirebaseFirestore.getInstance()
                .collection(LOGISTIC_PLAN)
                .document(documentId)
                .collection("logistik")
                .get().await()

            for (subDocument in subDocuments) {
                val idItem = subDocument.getString("idItem")
                val itemName = subDocument.getString("itemName")
                val quantity = subDocument.getLong("quantity")
                val stock = StockItem(
                    itemName = itemName ?: "",
                    quantity = quantity?.toInt() ?: 0,
                )
                logisticPlan.data.add(stock)
            }
            distributionPlan.add(logisticPlan)
        }
        distributionPlan.sortBy { it.time }
        return distributionPlan
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            (requireActivity() as BaseView).showLoading("")
            val plans = getAllLogisticPlan2()
            distributionPlanAdapter.addAll(plans)
            (requireActivity() as BaseView).hideLoading()
        }
    }
    private fun String.toValidString(): String{
        if(this=="null"){
            return "-"
        }
        return this
    }
}
