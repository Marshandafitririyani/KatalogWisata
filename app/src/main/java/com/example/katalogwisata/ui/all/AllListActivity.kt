package com.example.katalogwisata.ui.all

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.extension.openActivity
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.data.user.Tour
import com.example.katalogwisata.databinding.ActivityAllListBinding
import com.example.katalogwisata.databinding.ActivityDetailBinding
import com.example.katalogwisata.databinding.ItemListCatogariBinding
import com.example.katalogwisata.ui.detail.DetailActivity
import com.example.katalogwisata.ui.home.HomeActivity
import com.example.katalogwisata.ui.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllListActivity : BaseActivity<ActivityAllListBinding, AllListViewModel>(R.layout.activity_all_list) {

    private var tour = ArrayList<Tour?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Holder
        binding.rvListDestination.adapter =
            object : CoreListAdapter<ItemListCatogariBinding, Tour>(R.layout.item_list_catogari) {
                override fun onBindViewHolder(
                    holder: ItemViewHolder<ItemListCatogariBinding, Tour>,
                    position: Int
                ) {
                    val data = tour[position]
                    holder.binding.listTour = data

                    holder.binding.btnDetail.setOnClickListener {
                        openActivity<DetailActivity> {
                            putExtra(Const.TOUR.TOUR,data)

                        }
                    }
                }
            }.initItem(tour)

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        observe()

        val id = intent.getIntExtra(Const.CATEGORY.ID, 0)
        when (id) {
            1 -> {
                viewModel.listNature()
                binding.tvDestinasi.text = "Nature"
            }
            2 -> {
                viewModel.listPark()
                binding.tvDestinasi.text = "Park"
            }
            3 -> {
                getListAll()
                binding.tvDestinasi.text = "All"
            }
        }

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

    }
    private fun getListAll() {
        viewModel.listAll()
    }
    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.tour.observe(this@AllListActivity) {
                        tour.addAll(it)
                        binding.rvListDestination.adapter?.notifyDataSetChanged()

                    }
                }
            }
        }

    }
}
