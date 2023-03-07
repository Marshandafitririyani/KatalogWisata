package com.example.katalogwisata.api

import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/auth-login")
    suspend fun login(
        @Field("phone_number") phone: String?,
        @Field("password") password: String?
    ): String

    @FormUrlEncoded
    @POST("api/user-edit")
    suspend fun userUpdate(
        @Field("_method") method: String?,
        @Field("name") name: String?,
        @Field("phone number") phone: String?,
    ): String

    @Multipart
    @POST("api/user-edit")
    suspend fun userUpdateWithPhoto(
        @Part("_method") method: String,
        @Part("name") name: String,
        @Part("phone number") phone: String,
        @Part photo: MultipartBody.Part?
    ): String

    @POST("api/auth-logout")
    suspend fun logout(): String

    @GET("api/tour-list")
    suspend fun tourList(
    ): String

    @GET("api/tour-img-slider?limit=10")
    suspend fun imageSlider(
    ): String

    @GET("api/category-detail/{idCategory}")
    suspend fun tourCategory(
        @Path ("idCategory") idCategory: Int
    ): String

    @GET("api/category-detail/1")
    suspend fun tourCategoryNature(): String

    @GET("api/category-detail/2")
    suspend fun tourCategoryPark(): String

    @GET("api/category-detail/3")
    suspend fun tourCategoryAll(): String

    @FormUrlEncoded
    @POST("api/add-favourite")
    suspend fun postFavourite(
        @Field("tour_id") tour_id: Int?
    ): String
}




