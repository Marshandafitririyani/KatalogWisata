package com.example.katalogwisata.ui.home

import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.extension.openActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.data.image.ImageSlide
import com.example.katalogwisata.data.user.Tour
import com.example.katalogwisata.databinding.ActivityHomeBinding
import com.example.katalogwisata.databinding.ItemCardTourBinding
import com.example.katalogwisata.ui.all.AllListActivity
import com.example.katalogwisata.ui.detail.DetailActivity
import com.example.katalogwisata.ui.profile.ProfileActivity
import com.example.katalogwisata.ui.save.SaveTourActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    private var tour = ArrayList<Tour?>()
    private var tourAll = ArrayList<Tour?>()
    private var tourClass: Tour? = null

    override fun onStart() {
        val user = session.getUser()
        if (user != null) {
            binding.homeList = user
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageList = ArrayList<SlideModel>()

        observe()
        getTourList()
        getImage()

        binding.etSearchHome.doOnTextChanged { text, start, before, count ->
            if (text.isNullOrEmpty()) {
                tour.clear()
                binding.rvPopularTour.adapter?.notifyDataSetChanged()
                tour.addAll(tourAll)
                binding.rvPopularTour.adapter?.notifyItemInserted(0)
            } else {
                val filter = tourAll.filter { it?.name?.contains("$text", true) == true }
                tour.clear()
                filter.forEach {
                    tour.add(it)
                }
                binding.rvPopularTour.adapter?.notifyDataSetChanged()
                binding.rvPopularTour.adapter?.notifyItemInserted(0)
            }
        }

        binding.ivProfilHome.setOnClickListener {
            openActivity<ProfileActivity>()
        }
        binding.ivSave.setOnClickListener {
            openActivity<SaveTourActivity>()
        }

        binding.btnNature.setOnClickListener {
            openActivity<AllListActivity> {
                putExtra(Const.CATEGORY.ID, 1)
            }
        }

        binding.btnPark.setOnClickListener {
            openActivity<AllListActivity> {
                putExtra(Const.CATEGORY.ID, 2)
            }
        }

        binding.btnAll.setOnClickListener {
            openActivity<AllListActivity> {
                putExtra(Const.CATEGORY.ID, 3)
            }
        }

        binding.rvPopularTour.adapter =
            CoreListAdapter<ItemCardTourBinding, Tour>(R.layout.item_card_tour)
                .initItem(tour) { position, data ->
                    openActivity<DetailActivity> {
                        putExtra(Const.TOUR.TOUR, data)
                    }
                }

        val imageSlider = findViewById<ImageSlider>(R.id.image_slider)
        imageSlider.setImageList(imageList)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    getTourList()
                }
            }
        }

    }

    private fun getTourList() {
        viewModel.touList()
    }

    private fun observe() {
        viewModel.tour.observe(this) {
            tour.clear()
            tour.addAll(it)
            tourAll.clear()
            tourAll.addAll(it)
            binding?.rvPopularTour?.adapter?.notifyDataSetChanged()
            binding?.rvPopularTour?.adapter?.notifyItemInserted(0)

        }

        viewModel.image.observe(this) {
            initSlider(it)
        }
    }

    fun getImage() {
        viewModel.imageSlider()
    }

    private fun initSlider(data: List<ImageSlide>) {
        val imageList = ArrayList<SlideModel>()
        data.forEach {
            imageList.add(SlideModel(it.image, tourClass?.name))
        }
        binding.imageSlider.setImageList(imageList, ScaleTypes.CENTER_CROP)
    }
}



