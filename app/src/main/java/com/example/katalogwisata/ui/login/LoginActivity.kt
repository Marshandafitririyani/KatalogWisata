package com.example.katalogwisata.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.base64encrypt
import com.crocodic.core.extension.isEmptyRequired
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.textOf
import com.crocodic.core.helper.DateTimeHelper
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.databinding.ActivityLoginBinding
import com.example.katalogwisata.ui.home.HomeActivity
import com.example.katalogwisata.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity: BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.tvRegisterLogin.setOnClickListener {
            openActivity<RegisterActivity>() {
            }
        }

        binding.btnLogin.setOnClickListener {
            if (binding.etEmailLogin.isEmptyRequired(R.string.label_must_fill) || binding.etPasswordLogin.isEmptyRequired(
                    R.string.label_must_fill
                )
            ) {
                return@setOnClickListener
            }
            val email = binding.etEmailLogin.textOf()
            val password = binding.etPasswordLogin.textOf()

            viewModel.login(email, password)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.apiResponse.collect {
                        when (it.status) {
                            ApiStatus.LOADING -> loadingDialog.show(" Please Wait login")
                            ApiStatus.SUCCESS -> {
                                loadingDialog.dismiss()
                                openActivity<HomeActivity>()
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
