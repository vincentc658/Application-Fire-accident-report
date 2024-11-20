package com.app.fire.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import android.widget.AdapterView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.fire.R
import com.app.fire.databinding.ActivityMainBinding
import com.app.fire.model.UserModelFirestore
import com.app.fire.util.BaseView
import com.app.fire.util.SessionManager
import com.app.fire.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : BaseView() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.tvRegister.setOnClickListener {
            if (binding.etEmail.isTextNotEmpty() &&
                binding.etPassword.isTextNotEmpty() &&
                binding.etHp.isTextNotEmpty() &&
                binding.etName.isTextNotEmpty()
            ) {
                register(
                    binding.etEmail.getValue(),
                    binding.etPassword.getValue(),
                    binding.etName.getValue(),
                    binding.etHp.getValue()
                )
            }
        }
        binding.ivShowPass.setOnClickListener {
            if (binding.ivShowPass.tag == "0") {
                binding.ivShowPass.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.colorSecondary
                    ), PorterDuff.Mode.SRC_IN
                );
                binding.ivShowPass.tag = "1"
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
            } else {
                binding.ivShowPass.setColorFilter(
                    ContextCompat.getColor(
                        this,
                        R.color.light_gray_2
                    ), PorterDuff.Mode.SRC_IN
                );
                binding.ivShowPass.tag = "0"
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            binding.etPassword.setSelection(binding.etPassword.getValue().length)
        }
        if(SessionManager.getIsLogin(this)){
            goToPageAndClearPrevious(HomeActivity::class.java)
        }
    }

    private fun register(email: String, password: String, name: String, phone: String) {
        showLoading("Registering...")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val userData = UserModelFirestore(
                            id = it.uid,
                            name = name,
                            email = email,
                            noHp = phone,
                            typeUser = binding.spinnerUserType.selectedItemPosition
                        )

                        // Save user data to Firestore
                        userViewModel.registerToFirestore(
                            userId = it.uid,
                            userData = userData,
                            onSuccess = {
                                hideLoading()
                                showToast("Registration successful!")
                            },
                            onFailure = { e ->
                                hideLoading()
                                showToast("Failed to save user data: ${e.message}")
                            }
                        )
                    }
                } else {
                    hideLoading()
                    showToast(
                        task.exception?.message.toString()
                    )
                }
            }
    }
}