package com.app.fire.ui

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.R
import com.app.fire.adapter.StockAdapter
import com.app.fire.adapter.StockDistributionAdapter
import com.app.fire.databinding.ActivityListRoomBinding
import com.app.fire.databinding.FragmentListChatBinding
import com.app.fire.databinding.FragmentListOrganizationBinding
import com.app.fire.model.StockItem
import com.app.fire.util.BaseView
import com.google.firebase.firestore.FirebaseFirestore

class StockDistributionActivity : BaseView() {
    private lateinit var binding: ActivityListRoomBinding

    private lateinit var adapter: StockAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListRoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Pilih Logistik..."
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        adapter = StockAdapter(
            onClick = { stock ->
                val intent = Intent()
                intent.putExtra("stock", stock)
                setResult(RESULT_OK, intent)
                finish()
            }
        )

        // Setup RecyclerView
        binding.recyclerViewChats.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewChats.adapter = adapter
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