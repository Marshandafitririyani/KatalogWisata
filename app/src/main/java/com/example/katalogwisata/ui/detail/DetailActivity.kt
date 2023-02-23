package com.example.katalogwisata.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.data.user.Tour
import com.example.katalogwisata.databinding.ActivityDetailBinding
import com.example.katalogwisata.databinding.ActivityHomeBinding
import com.example.katalogwisata.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding, DetailViewModel>(R.layout.activity_detail)  {

    private var tour : Tour? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //data tour
        tour = intent.getParcelableExtra(Const.TOUR.TOUR)
        binding.detail = tour


    }
}