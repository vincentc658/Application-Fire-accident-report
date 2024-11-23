package com.app.fire.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.R
import com.app.fire.adapter.StockAdapter
import com.app.fire.adapter.StockDistributionAdapter
import com.app.fire.databinding.FragmentListChatBinding
import com.app.fire.databinding.FragmentListOrganizationBinding
import com.app.fire.model.AccidentModelFirestore
import com.app.fire.model.StockItem
import com.app.fire.ui.AddLogisticActivity
import com.app.fire.ui.FireAccidentActivity
import com.app.fire.ui.StockActivityInputActivity
import com.app.fire.util.BaseView
import com.google.firebase.firestore.FirebaseFirestore

class ListStockFragment : Fragment() {
    private lateinit var binding: FragmentListOrganizationBinding
    private lateinit var adapter :StockAdapter
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
        adapter= StockAdapter(context = requireActivity(), isShowDelete = true,
            onClick = { item ->
            }
        )
        // Setup RecyclerView
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = adapter
        binding.btnAdd.setOnClickListener {
//            (requireActivity() as BaseView).goToPage(AddLogisticActivity::class.java)
            (requireActivity() as BaseView).goToPage(StockActivityInputActivity::class.java)
        }
        getListLogistic()
    }

    override fun onResume() {
        super.onResume()
        getListLogistic()
    }

    private fun getListLogistic() {
        FirebaseFirestore.getInstance().collection("stockLogistic")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val accidents = ArrayList<StockItem>()
                    task.result?.forEach { document ->
                        val stockItem = StockItem(
                            itemName = document.data["itemName"].toString(),
                            quantity = document.data["quantity"].toString().toInt(),
                            distributionDate = document.data["distributionDate"].toString()
                        )
                        stockItem.id= document.id
                        accidents.add(stockItem)
                    }
                    adapter.addAll(accidents)
                }
            }

    }

}