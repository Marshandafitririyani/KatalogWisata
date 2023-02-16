package com.example.katalogwisata.ui.masuk

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.katalogwisata.R
import com.example.katalogwisata.ui.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@AndroidEntryPoint
class MasukActivity : AppCompatActivity() {

    private lateinit var buttonEnter: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_masuk)

        buttonEnter = findViewById(R.id.btn_enter)

        buttonEnter.setOnClickListener {
            val masuk = Intent(this, LoginActivity::class.java)
            startActivity(masuk)
        }
}
}