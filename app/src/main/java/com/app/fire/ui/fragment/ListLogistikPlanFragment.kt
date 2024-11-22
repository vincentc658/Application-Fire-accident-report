package com.app.fire.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.fire.adapter.OrganizationAdapter
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

class ListLogistikPlanFragment : Fragment() {
    companion object {
        val TAG = "homeMentor"
    }

    private lateinit var binding: FragmentListOrganizationBinding
    private lateinit var organizationViewModel: OrganizationViewModel
    private val adapterOrganizationAdapter: OrganizationAdapter by lazy {
        OrganizationAdapter(requireContext())
    }
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
        binding.btnAdd.setOnClickListener {
            distributionPlan.forEach {
                Log.e("getAllLogisticPlan", "data: ${it.location}")
                it.data.forEach {stock->
                    Log.e("getAllLogisticPlan", "data: ${stock.itemName} ${stock.quantity}")
                }
            }
            (requireActivity() as BaseView).goToPage(AddLogisticActivity::class.java)
        }
        getAllLogisticPlan()
    }

    private fun getAllLogisticPlan() {
        Log.e("getAllLogisticPlan", "start")
        FirebaseFirestore.getInstance().collection(LOGISTIC_PLAN)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val documentId = document.id // ID of the logisticPlan document
                    val logisticPlan = LogisticPlan(
                        location = document.data["location"].toString(),
                        accidentId = document.data["accidentId"].toString(),
                        timestamp = document.data["timestamp"].toString(),
                        time = document.data["time"].toString().toLong()
                    )

                    FirebaseFirestore.getInstance()
                        .collection(LOGISTIC_PLAN)
                        .document(documentId)
                        .collection("logistik") // Access the subcollection
                        .get()
                        .addOnSuccessListener { subDocuments ->
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
                        .addOnFailureListener { e ->
                            Log.e("FirestoreError", "Error retrieving logistik data: ${e.message}")
                        }
                    Log.e("getAllLogisticPlan", "---------------->>>>>>>>>>>>")

                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Error retrieving logisticPlan data: ${e.message}")
            }

    }
}