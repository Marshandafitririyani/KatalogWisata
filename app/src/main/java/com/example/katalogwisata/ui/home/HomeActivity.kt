package com.example.katalogwisata.ui.home

import android.os.Bundle
import com.crocodic.core.extension.openActivity
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.databinding.ActivityHomeBinding
import com.example.katalogwisata.ui.profile.ProfileActivity
import com.example.katalogwisata.ui.simpan.SaveWisataActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivProfilHome.setOnClickListener {
            openActivity<ProfileActivity> {

            }
        }

        binding.ivSave.setOnClickListener {
            openActivity<SaveWisataActivity>()
        }

//
//        val ghgfhgv = arrayOf("hgvghvghv", "vghvgj")
//
//        imageList.add(SlideModel(" Url Km "))
//        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
//        imageSlider.setImageList(imageList)
//

    }
}


