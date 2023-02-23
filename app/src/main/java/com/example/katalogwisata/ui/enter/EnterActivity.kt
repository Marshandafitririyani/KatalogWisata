package com.example.katalogwisata.ui.enter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.tos
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.databinding.ActivityEnterBinding
import com.example.katalogwisata.databinding.ActivityLoginBinding
import com.example.katalogwisata.ui.home.HomeActivity
import com.example.katalogwisata.ui.login.LoginActivity
import com.example.katalogwisata.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EnterActivity :  BaseActivity<ActivityEnterBinding, EnterViewModel>(R.layout.activity_enter) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnEnter.setOnClickListener {
            val isUser = session.getUser()
            if (isUser == null){
                openActivity< LoginActivity >()
            }else{
                openActivity< HomeActivity >()
            }
        }

//        val userLogin = session.getString(Const.USER.PROFILE)

//        tos("Cek Login: $userLogin")
/*
        binding.btnEnter.setOnClickListener {
            openActivity<LoginActivity> ()
        }*/
    }
}





//    private lateinit var buttonEnter: Button

//        buttonEnter = findViewById(R.id.btn_enter)
//
//        buttonEnter.setOnClickListener {
//            val masuk = Intent(this, LoginActivity::class.java)
//            startActivity(masuk)


/*
Handler(Looper.getMainLooper()).postDelayed({
    val isUser = session.getUser()
    if (isUser == null){
        openActivity<LoginActivity>()
    }else{
        openActivity<HomeActivity>()
    }
},4000)*/
