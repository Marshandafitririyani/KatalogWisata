package com.example.katalogwisata.ui.home

import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
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
import com.example.katalogwisata.databinding.ItemCardTourEndBinding
import com.example.katalogwisata.ui.all.AllListActivity
import com.example.katalogwisata.ui.detail.DetailActivity
import com.example.katalogwisata.ui.profile.ProfileActivity
import com.example.katalogwisata.ui.save.SaveTourActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding, HomeViewModel>(R.layout.activity_home) {

    private var tour = ArrayList<Tour?>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val imageList = ArrayList<SlideModel>()

        observe()
        getTourList()
        getImage()

//pengganti data base harus menyimpan pada session
        val user = session.getUser()
        if (user != null) {
            binding.homeList = user
        }

        binding.ivProfilHome.setOnClickListener {
            openActivity<ProfileActivity> {

            }
        }
        binding.ivSave.setOnClickListener {
            openActivity<SaveTourActivity>()
        }

        binding.btnNature.setOnClickListener{
            openActivity<AllListActivity> {
            putExtra(Const.CATEGORY.ID,1)}
        }

        binding.btnPark.setOnClickListener{
            openActivity<AllListActivity> {
                putExtra(Const.CATEGORY.ID,2)
            }
        }

        binding.btnAll.setOnClickListener {
            openActivity<AllListActivity> {
                putExtra(Const.CATEGORY.ID,3)
            }
        }

        binding.ivViewAll.setOnClickListener {
            openActivity<AllListActivity> {
                putExtra(Const.CATEGORY.ID,3)
            }
        }

        //Adapter List Tour
        binding.rvPopularTour.adapter = CoreListAdapter<ItemCardTourEndBinding, Tour>(R.layout.item_card_tour_end)
            .initItem(tour) { position, data ->
                openActivity<DetailActivity> {
                    putExtra(Const.TOUR.TOUR, data)
                }
            }

        Glide
            .with(this)
            .load(user?.image)
            .into(binding.ivProfilHome)

        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        imageSlider.setImageList(imageList)


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                launch {
                   getTourList()
                }
            }
        }

    }

    private fun getTourList() {
        viewModel.touList()
    }
    private fun observe(){
        viewModel.tour.observe(this){
            tour.addAll(it)
            binding.rvPopularTour.adapter?.notifyDataSetChanged()
        }
        viewModel.image.observe(this){
            initSlider(it)
        }
    }
fun getImage(){
    viewModel.imageSlider()
}

    private fun initSlider(data: List<ImageSlide>){
        val imageList = ArrayList<SlideModel>()
        data.forEach{
            imageList.add(SlideModel(it.image))
        }
        binding.imageSlider.setImageList(imageList,ScaleTypes.CENTER_CROP)
    }
}



