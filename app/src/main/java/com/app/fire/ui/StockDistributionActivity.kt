package com.app.fire.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.R
import com.app.fire.adapter.ChatAdapter
import com.app.fire.adapter.StockDistributionAdapter
import com.app.fire.databinding.FragmentListChatBinding
import com.app.fire.model.StockItem
import com.app.fire.util.BaseView

class StockDistributionActivity : BaseView() {
    private lateinit var binding: FragmentListChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentListChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }

        // Sample Data
        val stockList = listOf(
            StockItem(
                "Food Supplies",
                "50 Packages",
                "12 Nov 2024",
                "John Doe",
                R.drawable.ic_inbox_24
            ),
            StockItem("Blankets", "30 Pieces", "10 Nov 2024", "Jane Smith", R.drawable.ic_inbox_24),
            StockItem(
                "Medicine Kits",
                "20 Kits",
                "08 Nov 2024",
                "Mike Johnson",
                R.drawable.ic_inbox_24
            )
        )

        // Setup RecyclerView
        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = StockDistributionAdapter(stockList)
    }
}