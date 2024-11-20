package com.app.fire.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.R
import com.app.fire.adapter.StockDistributionAdapter
import com.app.fire.databinding.FragmentListChatBinding
import com.app.fire.databinding.FragmentListOrganizationBinding
import com.app.fire.model.StockItem

class ListStockFragment : Fragment() {
    private lateinit var binding: FragmentListOrganizationBinding
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
        binding.rv.layoutManager = LinearLayoutManager(requireContext())
        binding.rv.adapter = StockDistributionAdapter(stockList)
    }
}