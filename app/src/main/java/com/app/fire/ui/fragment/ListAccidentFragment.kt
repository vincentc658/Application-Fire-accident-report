package com.app.fire.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.AccidentAdapter
import com.app.fire.adapter.OrganizationAdapter
import com.app.fire.databinding.FragmentListAccidentBinding
import com.app.fire.model.OrganizationModelFirestore

class ListAccidentFragment  : Fragment() {
    companion object {
        val TAG = "homeMentor"
    }
    private lateinit var binding: FragmentListAccidentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListAccidentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapterOrganization = AccidentAdapter(requireContext())
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterOrganization
        }
        adapterOrganization.addAll(arrayListOf(OrganizationModelFirestore(), OrganizationModelFirestore()))

    }

    fun String?.checkIsEmptyOrNull(): Boolean {
        if (this == "null") {
            return true
        }
        if (isNullOrEmpty()) {
            return true
        }
        return false
    }

    override fun onResume() {
        super.onResume()
    }
}