package com.app.fire.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.OrganizationAdapter
import com.app.fire.databinding.FragmentListOrganizationBinding
import com.app.fire.model.OrganizationModelFirestore
import com.app.fire.ui.InputOrganizationActivity
import com.app.fire.util.BaseView
import com.app.fire.viewModel.OrganizationViewModel

class ListOrganizationFragment : Fragment() {
    companion object {
        val TAG = "homeMentor"
    }

    private lateinit var binding: FragmentListOrganizationBinding
    private lateinit var organizationViewModel: OrganizationViewModel
    private val adapterOrganizationAdapter : OrganizationAdapter by lazy {
         OrganizationAdapter(requireContext())
    }
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
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterOrganizationAdapter
        }
        binding.btnAdd.setOnClickListener {
            (requireActivity() as BaseView).goToPage(InputOrganizationActivity::class.java)
        }
//        adapterOrganizationAdapter.addAll(arrayListOf(OrganizationModelFirestore(), OrganizationModelFirestore()))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        organizationViewModel = ViewModelProvider(this)[OrganizationViewModel::class.java]
        organizationViewModel.fetchOrganizations()
        organizationViewModel.userLiveData.observe(this){
            (requireActivity() as BaseView).hideLoading()
            adapterOrganizationAdapter.addAll(it)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as BaseView).showLoading("Loading")
        organizationViewModel.fetchOrganizations()
    }
}