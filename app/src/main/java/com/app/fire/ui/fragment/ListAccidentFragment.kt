package com.app.fire.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.AccidentAdapter
import com.app.fire.databinding.FragmentListAccidentBinding
import com.app.fire.model.AccidentModelFirestore
import com.app.fire.model.OrganizationModelFirestore
import com.app.fire.ui.FireAccidentActivity
import com.app.fire.util.BaseView
import com.app.fire.util.SessionManager
import com.google.firebase.firestore.FirebaseFirestore

class ListAccidentFragment : Fragment() {
    companion object {
        val TAG = "homeMentor"
    }

    private lateinit var adapterOrganization: AccidentAdapter

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
        adapterOrganization = AccidentAdapter(requireContext())
        binding.rv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterOrganization
        }
        if (SessionManager.getTypeUser(requireContext()) == 1) {
            binding.btnAdd.visibility = View.VISIBLE
        } else {
            binding.btnAdd.visibility = View.GONE
        }
        binding.btnAdd.setOnClickListener {
            (requireActivity() as BaseView).goToPage(FireAccidentActivity::class.java)
        }
        fetchChatMessages()
    }

    private fun fetchChatMessages() {
        FirebaseFirestore.getInstance().collection("fire_accidents")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val accidents = ArrayList<AccidentModelFirestore>()
                    task.result?.forEach { document ->
                        val accidentModelFirestore = AccidentModelFirestore(
                            rumahRusak = document.data["rumahRusak"].toString().toInt(),
                            korbanTerdampak = document.data["korbanTerdampak"].toString().toInt(),
                            jumlahKK = document.data["jumlahKK"].toString().toInt(),
                            lokasi = document.data["lokasi"].toString(),
                            waktu = document.data["waktu"].toString(),
                            time = document.data["time"].toString().toLong()
                        )
                        accidents.add(accidentModelFirestore)
                    }
                    val sortedBy =accidents.sortedBy { it.time }
                    adapterOrganization.addAll(sortedBy as ArrayList<AccidentModelFirestore>)
                }
            }

    }

    override fun onResume() {
        super.onResume()
        fetchChatMessages()
    }
}