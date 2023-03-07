package com.example.katalogwisata.ui.all

import android.os.Bundle
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
import com.example.katalogwisata.databinding.ItemListCatogariBinding
import com.example.katalogwisata.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AllListActivity :
    BaseActivity<ActivityAllListBinding, AllListViewModel>(R.layout.activity_all_list) {

    private var tour = ArrayList<Tour?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        observe()
        getData()


        binding.swipeRefreshLayout.setOnRefreshListener {
            val id = intent.getIntExtra(Const.CATEGORY.ID, 0)
            when (id) {
                1 -> {
                    viewModel.listNature()
                }
                2 -> {
                    viewModel.listPark()
                }
                3 -> {
                    viewModel.listAll()
                }

            }

        }

        binding.rvListDestination.adapter =
            object : CoreListAdapter<ItemListCatogariBinding, Tour>(R.layout.item_list_catogari) {
                override fun onBindViewHolder(
                    holder: ItemViewHolder<ItemListCatogariBinding, Tour>,
                    position: Int
                ) {
                    val data = tour[position]
                    holder.binding.data = data

                    holder.binding.cardTour.setOnClickListener {
                        openActivity<DetailActivity> {
                            putExtra(Const.TOUR.TOUR, data)
                        }
                    }
                }
            }.initItem(tour)
        binding.btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getData(){
        val idCategory = intent.getIntExtra(Const.CATEGORY.ID, 0)
        viewModel.tourListPath(idCategory)

        binding.tvDestination.text = when (idCategory) {
            1 -> "Nature"
            2 -> "Park"
            3 -> "All"
            else-> ""
        }
    }

    private fun observe() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.tour.observe(this@AllListActivity) {
                        binding.swipeRefreshLayout.isRefreshing = false
                        tour.clear()
                        tour.addAll(it)
                        binding.rvListDestination.adapter?.notifyDataSetChanged()


                    }
                }
            }
        }

    }
}
