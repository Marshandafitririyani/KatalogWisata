package com.example.katalogwisata.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyCharacterMap.load
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.PointerIconCompat.load
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.crocodic.core.extension.openActivity
import com.crocodic.core.extension.tos
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.data.user.Session
import com.example.katalogwisata.databinding.ActivityProfileBinding
import com.example.katalogwisata.ui.edit.EditProfileActivity
import com.example.katalogwisata.ui.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.System.load
import javax.inject.Inject

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

        val user = session.getUser()
        if (user != null){
            binding.user = user
        }

        binding.ivLogout.setOnClickListener {
            viewModel.logout { tos("Logout") }
        }


        /*lifecycleScope.launch {
            viewModel.getUser.observe(ProfileActivity()) {
                it?.let { data ->
                    binding?.user = data
                    binding?.let { viewImage ->
                        //untuk profilenya
                        Glide
                            .with(ProfileActivity())
                            .load(it.photo)
                            .placeholder(R.drawable.go_tour)
                            .error(R.drawable.errornotfound)
                            .apply(RequestOptions.centerInsideTransform())
                            .into(viewImage.ivProfil)
                    }
                }
            }
        }*/
    }

}