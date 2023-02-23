package com.example.katalogwisata.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.crocodic.core.extension.openActivity
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.databinding.ActivityProfileBinding
import com.example.katalogwisata.ui.edit.EditProfileActivity
import com.example.katalogwisata.ui.enter.EnterActivity
import com.example.katalogwisata.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileActivity : BaseActivity<ActivityProfileBinding, ProfileViewModel>(R.layout.activity_profile) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.ivBackProfil.setOnClickListener {
            openActivity<HomeActivity> ()
        }

        binding.btnEditProfil.setOnClickListener {
            openActivity<EditProfileActivity>()
        }

        binding.btnEditProfil.setOnClickListener {
            val kembali = Intent(this, EditProfileActivity::class.java).apply {
                putExtra("username", binding?.user?.name, )
                putExtra("phoneNumber", binding?.user?.phoneNumber)
            }
            startActivity(kembali)
        }


        val user = session.getUser()
        if (user != null){
            binding.user = user
        }

        Glide
            .with(this)
            .load(user?.image)
            .error(R.drawable.img_person)
            .into(binding.ivProfil)


        binding.ivLogout.setOnClickListener {
            viewModel.logout()
            openActivity<EnterActivity> ()
        }

    }

}