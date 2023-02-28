package com.example.katalogwisata.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.data.user.Tour
import com.example.katalogwisata.databinding.ActivityDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity :
    BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail) {

    private var tour: Tour? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tour = intent.getParcelableExtra(Const.TOUR.TOUR)
        binding.detail = tour

        initialButtonLike()

        binding.btnRute.setOnClickListener {
            sendLocationIntent()
        }

        binding.btnSave.setOnClickListener {
            val tourId = tour?.id
            if (tourId != null) {
                viewModel.favouriteTour(tourId)
            }
        }
        binding.btnUnsave.setOnClickListener {
            val tourId = tour?.id
            if (tourId != null) {
                viewModel.favouriteTour(tourId)
            }
        }

    }

    private fun initialButtonLike() {
        if (tour?.like.equals("true")) {
            binding.btnSave.visibility = View.GONE
            binding.btnUnsave.visibility = View.VISIBLE
        } else {
            binding.btnSave.visibility = View.VISIBLE
            binding.btnUnsave.visibility = View.GONE
        }
    }

    private fun sendLocationIntent() {
        val intentUri = Uri.parse("google.navigation:q=${tour?.name}&mode=d")
        val mapIntent = Intent(Intent.ACTION_VIEW, intentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)


    }
}

