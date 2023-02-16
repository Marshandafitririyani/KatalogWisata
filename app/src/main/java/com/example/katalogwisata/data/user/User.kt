package com.example.katalogwisata.data.user

import android.os.Parcelable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.katalogwisata.R
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class User(
    @PrimaryKey
    @Expose
    @SerializedName("id_room")
    val idRoom: Int,
    @Expose
    @SerializedName("id")
    val id: String,
    @Expose
    @SerializedName("photo")
    val photo : String?,
    @Expose
    @SerializedName("name")
    val name : String,
    @Expose
    @SerializedName("email")
    val email : String,

): Parcelable {
    companion object {

        @JvmStatic
        @BindingAdapter(value = ["image", "imageThumbnail"], requireAll = false)
        fun loadImage(imageView: ImageView, image: String?, imageThumbnail: String?) {
            image?.let{
                val thumbnail = Glide
                    .with(imageView.context)
                    .load(imageThumbnail)
                    .placeholder(R.drawable.loding_image)
                    .apply(RequestOptions.centerCropTransform())

                Glide
                    .with(imageView.context)
                    .load(image)
                    .placeholder(R.drawable.loding_image)
                    .error(R.drawable.errornotfound)
                    .thumbnail(thumbnail)
                    .apply(RequestOptions.centerCropTransform())
                    .into(imageView)
}
        }
    }
}