package com.example.katalogwisata.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.crocodic.core.extension.tos
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.databinding.ActivityRegisterBinding
import com.example.katalogwisata.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity :  BaseActivity<ActivityRegisterBinding, RegisterViewModel>(R.layout.activity_register) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivBack.setOnClickListener {
            openActivity<LoginActivity>() {
            }
        }
        binding.btnSignUp.setOnClickListener {
            if (binding.etFullName.isEmptyRequired(R.string.label_must_fill) ||
                binding.etEmailRegister.isEmptyRequired(R.string.label_must_fill) ||
                binding.etPasswordRegister.isEmptyRequired(R.string.label_must_fill)
            ) {
                return@setOnClickListener
            }
            val name = binding.etFullName.textOf()
            val email = binding.etEmailRegister.textOf()
            val password = binding.etPasswordRegister.textOf()

            viewModel.register(name, email, password)
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show("Please Wait Register")
                            ApiStatus.SUCCESS -> { loadingDialog.show("Succes")
                                openActivity<LoginActivity>()
                                tos("Please Login")
                                finish()
                            }
                            else -> loadingDialog.setResponse(it.message ?: return@collect)
                        }
                    }
                }
            }
        }
    }
}