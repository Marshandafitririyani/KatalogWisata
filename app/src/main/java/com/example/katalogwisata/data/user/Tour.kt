package com.example.katalogwisata.data.user

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity
data class Tour(
    @Expose
    @SerializedName("id")
    val id: String?,
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("image")
    val image: String,
    @Expose
    @SerializedName("category_id")
    val categoryId: String,
    @Expose
    @SerializedName("information")
    val information: String,
    @Expose
    @SerializedName("address")
    val address: String,
    @Expose
    @SerializedName("latitude")
    val latitude: String,
    @Expose
    @SerializedName("longitude")
    val longitude: String,
    @Expose
    @SerializedName("created_at")
    val createdAt: String,
    @Expose
    @SerializedName("updated_at")
    val updatedAt: String,
    @Expose
    @SerializedName("like")
    val like: String,

    ): Parcelable