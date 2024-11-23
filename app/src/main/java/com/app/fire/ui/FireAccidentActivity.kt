package com.app.fire.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import com.app.fire.databinding.ActivityFireAccidentBinding
import com.app.fire.util.BaseView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class FireAccidentActivity : BaseView() {

    private lateinit var binding: ActivityFireAccidentBinding
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFireAccidentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="Laporan Kebakaran"
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
        var selectedDate: Date? = null

        // Setup DatePickerDialog for waktu
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

        // Submit Button Click Listener
        binding.btnSubmit.setOnClickListener {
            val rumahRusak = binding.etRumahRusak.text.toString().trim()
            val korbanTerdampak = binding.etKorbanTerdampak.text.toString().trim()
            val jumlahKK = binding.etJumlahKK.text.toString().trim()
            val lokasi = binding.etLokasi.text.toString().trim()
            val jam = binding.etJam.text.toString().trim()

            if (rumahRusak.isEmpty() || korbanTerdampak.isEmpty() || jumlahKK.isEmpty() || lokasi.isEmpty()|| jam.isEmpty() || selectedDate == null) {
                Toast.makeText(this, "Harap isi semua data!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save data to Firestore
            val fireAccidentData = mapOf(
                "rumahRusak" to rumahRusak.toInt(),
                "korbanTerdampak" to korbanTerdampak.toInt(),
                "jumlahKK" to jumlahKK.toInt(),
                "lokasi" to lokasi,
                "jam" to jam,
                "waktu" to formatDate(selectedDate!!),
                "time" to System.currentTimeMillis()
            )
            showLoading("")
            firestore.collection("fire_accidents")
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
