package com.example.katalogwisata.ui.save

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.crocodic.core.base.adapter.CoreListAdapter
import com.crocodic.core.extension.openActivity
import com.example.katalogwisata.R
import com.example.katalogwisata.data.base.BaseActivity
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.data.user.Tour
import com.example.katalogwisata.databinding.ActivitySaveTourBinding
import com.example.katalogwisata.databinding.ItemListCatogariBinding
import com.example.katalogwisata.ui.detail.DetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SaveTourActivity :
    BaseActivity<ActivitySaveTourBinding, SaveTourViewModel>(R.layout.activity_save_tour) {

    private var tour = ArrayList<Tour?>()

    override fun onStart() {
        val user = session.getUser()
        if (user != null) {
            binding.data = user
        }
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getTourList()
        observe()

        binding.rvTourSave.adapter =
            CoreListAdapter<ItemListCatogariBinding, Tour>(R.layout.item_list_catogari)
                .initItem(tour) { position, data ->
                    openActivity<DetailActivity> {
                        putExtra(Const.TOUR.TOUR, data)
                    }
                }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    getTourList()
                }
            }
        }

    }

    private fun getTourList() {
        viewModel.tourList()
    }

    private fun observe() {
        viewModel.tour.observe(this) {
            val filterByLike = it.filter { it.like.contains("true", true) }
            tour.clear()
            tour.addAll(filterByLike)
            binding?.rvTourSave?.adapter?.notifyDataSetChanged()
            binding?.rvTourSave?.adapter?.notifyItemInserted(0)
            if (tour.isEmpty()) {
                binding.tvEmptySave.visibility = View.VISIBLE
            } else {
                binding.tvEmptySave.visibility = View.GONE
            }

        }
    }
}