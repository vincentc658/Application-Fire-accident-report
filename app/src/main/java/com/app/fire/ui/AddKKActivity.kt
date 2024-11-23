package com.app.fire.ui

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.StockAdapter
import com.app.fire.databinding.ActivityAddKkBinding
import com.app.fire.model.AccidentModelFirestore
import com.app.fire.model.StockItem
import com.app.fire.util.BaseView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Date

class AddKKActivity : BaseView() {
    private lateinit var adapter: StockAdapter
    private lateinit var binding: ActivityAddKkBinding
    private val firestore = FirebaseFirestore.getInstance()
    private var planId = ""
    var selectedDate: Date? = null
    val accidents = ArrayList<AccidentModelFirestore>()
    override fun onGetData(intent: Intent?, requestCode: Int) {
        super.onGetData(intent, requestCode)
        if (requestCode == 100) {
            val stock = intent?.getParcelableExtra<StockItem>("stock")
            stock?.let {
                if (!adapter.getData().map { it.itemName }.contains(stock.itemName)) {
                    adapter.add(stock)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddKkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Input Nama KK"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Go back to the previous screen
        }
        binding.btnSubmit.setOnClickListener {
            goToPageActivityResult(StockDistributionActivity::class.java, 100)
        }
        adapter = StockAdapter(this, isShowCountItem = true,
            onClick = { item ->
            })

        // Setup RecyclerView
        binding.rvData.layoutManager = LinearLayoutManager(this)
        binding.rvData.adapter = adapter
        binding.btnAddPlan.setOnClickListener {
            if (binding.etNamaKK.isTextNotEmpty()) {
                addKK()
            }
        }
    }

    private fun addKK() {
        showLoading("")
        val newRoomRef = firestore.collection(KK).document()
        planId = newRoomRef.id

        val newPlan = mapOf(
            "namaKK" to binding.etNamaKK.text.toString(), // You can customize this
            "time" to System.currentTimeMillis()
        )

        // Save the new room and the first message
        newRoomRef.set(newPlan)
            .addOnSuccessListener {
                adapter.getData().forEach {
                    save(it)
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        hideLoading()
                        finish()
                    }
                }
            }
            .addOnFailureListener { e ->
                showToast("Failed to create Plan: ${e.message}")
            }
    }

    private fun save(stock: StockItem) {
        if (planId == null) return // Safety check

        val chatData = hashMapOf(
            "itemName" to stock.itemName,
            "quantity" to stock.quantityPlan,
            "idItem" to stock.id
        )

        // Save the message to the "chats" subcollection
        firestore.collection(KK)
            .document(planId)
            .collection(LOGISTIC)
            .add(chatData)
            .addOnSuccessListener {
            }
            .addOnFailureListener { e ->
                showToast("Failed to create Plan: ${e.message}")
            }
    }
}