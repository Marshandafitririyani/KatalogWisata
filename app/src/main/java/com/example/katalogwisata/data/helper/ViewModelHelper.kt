package com.example.katalogwisata.data.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.katalogwisata.R


class ViewModelHelper {
    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imageUrl"], requireAll = false)
        fun loadImageRecipe(view: ImageView, imageUrl: String?) {

            view.setImageDrawable(null)

            imageUrl?.let {
                Glide
                    .with(view.context)
                    .load(imageUrl)
                    .placeholder(R.drawable.img_person)
                    .error(R.drawable.img_error_not_found)
                    .into(view)

            }

        }

    }

}