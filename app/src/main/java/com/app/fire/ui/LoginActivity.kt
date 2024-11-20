package com.app.fire.ui

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.app.fire.R
import com.app.fire.databinding.ActivityLoginBinding
import com.app.fire.databinding.PopupForgetPasswordBinding
import com.app.fire.util.BaseView
import com.app.fire.util.SessionManager
import com.app.fire.viewModel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
class LoginActivity : BaseView() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private var tableName = ""
    private var typeUser = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        setContentView(binding.root)
        auth = Firebase.auth
        auth.firebaseAuthSettings.setAppVerificationDisabledForTesting(true)
        typeUser = intent.getIntExtra("typeUser", TYPE_STUDENT)
//        when (typeUser) {
//            TYPE_MENTOR -> {
//                tableName = TABLE_MENTOR
//                binding.tvTitle.text = "Login sebagai Mentor"
//            }
//
//            TYPE_STUDENT -> {
//                tableName = TABLE_CUSTOMER
//                binding.tvTitle.text = "Login sebagai Pelajar"
//            }
//
//            else -> {
//                binding.tvTitle.text = "Login sebagai Admin"
//                binding.clParent.setBackgroundColor(
//                    ContextCompat.getColor(
//                        this,
//                        R.color.colorSecondary
//                    )
//                )
//                binding.tvStart.background =
//                    ContextCompat.getDrawable(this, R.drawable.background_8_primary)
//            }
//        }
        binding.tvRegister.setOnClickListener {
                goToPage(RegisterActivity::class.java)
        }
        binding.tvStart.setOnClickListener {

            if (binding.etEmail.isTextNotEmpty() && binding.etPassword.isTextNotEmpty()) {
//                if (typeUser == TYPE_ADMIN) {
//                    loginAdmin(
//                        binding.etEmail.getValue(),
//                        binding.etPassword.getValue().toMd5Hash()
//                    )
//                    return@setOnClickListener
//                }
                login(binding.etEmail.getValue(), binding.etPassword.getValue())
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
        binding.tvForgetPass.setOnClickListener {
            showDialogForgetPass()
        }
        userViewModel.userLiveData.observe(this) { user ->
            hideLoading()
            user?.let {
                SessionManager.setCustData(this,
                    it.name,
                    it.id,
                    it.email,
                    it.noHp,
                    it.avatar,
                    it.typeUser)
                showToast("Wellcom, ${it.name}")
                goToPageAndClearPrevious(HomeActivity::class.java)
            }
        }

        // Observe LiveData for errors
        userViewModel.errorLiveData.observe(this) { error ->
            error?.let {
                hideLoading()
                showToast("Error: $it")
            }
        }
        if(SessionManager.getIsLogin(this)){
            goToPageAndClearPrevious(HomeActivity::class.java)
        }
    }

    private fun login(email: String, password: String) {
        showLoading("Login")
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        userViewModel.fetchUser(it.uid)
                    }
                } else {
                    hideLoading()
                    showToast(
                        task.exception?.message.toString()
                    )
                }
            }
    }


    private fun showDialogForgetPass() {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        val binding: PopupForgetPasswordBinding = PopupForgetPasswordBinding
            .inflate(LayoutInflater.from(this))
        builder.setView(binding.root)
        alertDialog = builder.create()
        binding.tvSubmit.setOnClickListener { _: View? ->
            if(binding.etEmail.isTextNotEmpty()){
                forgotPassword(binding.etEmail.getValue())
                alertDialog.dismiss()
            }
        }
        alertDialog.show()
    }

    private fun forgotPassword(email: String) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener {
            if (it.isSuccessful) {
                showToast("Check email to reset password")
            } else {
                showToast("Failed to reset password")
            }
        }
    }
}