package com.app.fire.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import com.app.fire.databinding.ActivityFireAccidentBinding
import com.app.fire.databinding.ActivityStockInputBinding
import com.app.fire.util.BaseView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StockActivityInputActivity: BaseView() {

    private lateinit var binding: ActivityStockInputBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStockInputBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Laporan Kebakaran"
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }


        binding.btnSubmit.setOnClickListener {
            val stockName = binding.etStockName.text.toString().trim()
            val stockTotal = binding.etStockTotal.text.toString().trim()

            if (stockName.isEmpty() || stockTotal.isEmpty()) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save data to Firestore
            val fireAccidentData = mapOf(
                "itemName" to stockName,
                "quantity" to stockTotal.toInt(),
                "distributionDate" to formatDate(Date(System.currentTimeMillis())),
                "time" to System.currentTimeMillis()
            )
            showLoading("")
            firestore.collection("stockLogistic")
                .add(fireAccidentData)
                .addOnSuccessListener {
                    hideLoading()
                    Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    finish() // Close activity
                }
                .addOnFailureListener { e ->
                    hideLoading()
                    Toast.makeText(this, "Gagal menyimpan data: ${e.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
    fun formatDate(date: Date): String {
        val formatter = SimpleDateFormat("d MMMM yyyy", Locale("id", "ID")) // Indonesian locale for correct month names
        return formatter.format(date)
    }
}
