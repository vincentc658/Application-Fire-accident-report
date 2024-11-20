package com.app.fire.ui

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.app.fire.databinding.ActivityInputOrganizationBinding
import com.app.fire.model.OrganizationModelFirestore
import com.app.fire.util.BaseView
import com.app.fire.viewModel.OrganizationViewModel

class InputOrganizationActivity : BaseView() {
    private lateinit var organizationViewModel: OrganizationViewModel
    private lateinit var binding: ActivityInputOrganizationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputOrganizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        organizationViewModel = ViewModelProvider(this)[OrganizationViewModel::class.java]
        binding.apply {

            btnSave.setOnClickListener {
                val organizationName = etOrganizationName.text.toString().trim()
                val email = etEmail.text.toString().trim()
                val contactNumber = etContactNumber.text.toString().trim()
                if (organizationName.isBlank() || email.isBlank() || contactNumber.isBlank()) {
                    Toast.makeText(
                        this@InputOrganizationActivity,
                        "Please fill in all fields.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    testin(OrganizationModelFirestore(organizationName, email, contactNumber))
                }
            }
        }
    }
    private fun testin(organization : OrganizationModelFirestore){
        showLoading("Loading")
        organizationViewModel.addOrganizationToFirestore(
            userData = organization,
            onSuccess = {
                hideLoading()
                Toast.makeText(
                    this@InputOrganizationActivity,
                    "Organization Added!",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            },
            onFailure = { e ->
                hideLoading()
                showToast("Failed to save user data: ${e.message}")
            }
        )
    }
}