package com.example.katalogwisata.ui.enter

import android.os.Bundle
import com.crocodic.core.extension.openActivity
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.databinding.ActivityEnterBinding
import com.example.katalogwisata.ui.home.HomeActivity
import com.example.katalogwisata.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterActivity : BaseActivity<ActivityEnterBinding, EnterViewModel>(R.layout.activity_enter) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnEnter.setOnClickListener {
            val isUser = session.getUser()
            if (isUser == null) {
                openActivity<LoginActivity>()
            } else {
                openActivity<HomeActivity>()
            }
        }
    }
}






