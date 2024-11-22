package com.app.fire.ui

import android.R
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.fire.adapter.StockAdapter
import com.app.fire.databinding.ActivityAddLogisticBinding
import com.app.fire.model.AccidentModelFirestore
import com.app.fire.model.StockItem
import com.app.fire.util.BaseView
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

class AddLogisticActivity : BaseView() {
    private lateinit var adapter: StockAdapter
    private lateinit var binding: ActivityAddLogisticBinding
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
        binding = ActivityAddLogisticBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Input Rencana Logistik"
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Go back to the previous screen
        }
        binding.btnSubmit.setOnClickListener {
            goToPageActivityResult(StockDistributionActivity::class.java, 100)
        }
        binding.tvWaktu.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.tvWaktu.text = formattedDate

                // Set selectedDate
                val cal = Calendar.getInstance()
                cal.set(selectedYear, selectedMonth, selectedDay)
                selectedDate = cal.time
            }, year, month, day)
            datePicker.show()
        }
        adapter = StockAdapter(true,
            onClick = { item ->
            })

        // Setup RecyclerView
        binding.rvData.layoutManager = LinearLayoutManager(this)
        binding.rvData.adapter = adapter
        binding.btnAddPlan.setOnClickListener {
            createDistributionPlan()
        }
        fetchChatMessages()
    }

    private fun fetchChatMessages() {
        FirebaseFirestore.getInstance().collection("fire_accidents")
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.forEach { document ->
                        val accidentModelFirestore = AccidentModelFirestore(
                            rumahRusak = document.data["rumahRusak"].toString().toInt(),
                            korbanTerdampak = document.data["korbanTerdampak"].toString().toInt(),
                            jumlahKK = document.data["jumlahKK"].toString().toInt(),
                            lokasi = document.data["lokasi"].toString(),
                            waktu = document.data["waktu"].toString(),
                            time = document.data["time"].toString().toLong(),
                        )
                        accidentModelFirestore.id = document.id
                        accidents.add(accidentModelFirestore)
                    }

                    val adapter = ArrayAdapter(
                        this,
                        R.layout.simple_spinner_item,
                        accidents.map { it.lokasi })
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerUserType.adapter = adapter
                }
            }
    }

    private fun createDistributionPlan() {
        showLoading("")
        // Generate a new room ID
        val newRoomRef = firestore.collection(LOGISTIC_PLAN).document()
        planId = newRoomRef.id

        val newPlan = mapOf(
            "location" to binding.spinnerUserType.selectedItem.toString(), // You can customize this
            "accidentId" to accidents[binding.spinnerUserType.selectedItemPosition].id, // You can customize this
            "time" to selectedDate?.time,
            "timestamp" to binding.tvWaktu.text.toString()
        )

        // Save the new room and the first message
        newRoomRef.set(newPlan)
            .addOnSuccessListener {
                adapter.getData().forEach {
                    saveChatToFirestore(it)
                    hideLoading()
                    showToast("Success add plan")
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(2000)
                        finish()
                    }
                }
            }
            .addOnFailureListener { e ->
                showToast("Failed to create Plan: ${e.message}")
            }
    }

    private fun saveChatToFirestore(stock: StockItem) {
        if (planId == null) return // Safety check

        val chatData = hashMapOf(
            "itemName" to stock.itemName,
            "quantity" to stock.quantityPlan,
            "idItem" to stock.id
        )

        // Save the message to the "chats" subcollection
        firestore.collection(LOGISTIC_PLAN)
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