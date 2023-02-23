package com.example.katalogwisata.api

import okhttp3.MultipartBody
import retrofit2.http.*
import java.io.File

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
        @Field("name") name : String?,
        @Field("phone number") phone : String?,
    ): String

    @Multipart
    @POST("api/user-edit")
    suspend fun userUpdateWithPhoto(
        @Part("_method") method: String,
        @Part("name") name : String,
        @Part("phone number") phone : String,
        @Part photo : MultipartBody.Part?
    ) : String

    @POST("api/auth-logout")
    suspend fun logout() : String

    @GET("api/tour-list")
    suspend fun tourList(
    ) : String

    //Image Slider
    @GET("api/tour-img-slider?limit=8")
    suspend fun imageSlider(
    ) : String

    @GET("api/category-list")
    suspend fun categoryList(
    ) : String

    @GET("api/category-detail/1")
    suspend fun tourCategoryNature() : String

    @GET("api/category-detail/2")
    suspend fun tourCategoryPark() : String

    @GET("api/category-detail/3")
    suspend fun tourCategoryAll() : String


}


/*
    @Multipart
    @PATCH("api/member/2")
    suspend fun updateProfileWithPhoto(
        @Part("_method") method: String?,
        @Part("name") name: String?,
        @Part image: MultipartBody.Part?,
        @Part("email") email: String?,
        @Part("password") password: String?
    ): String
*/

   /* @POST("api/logout")
    suspend fun logout(): String

    @GET("api/member/2")
    suspend fun memberDetail(): String

    @GET("api/wisata")
    suspend fun getWisataList(): String

    @FormUrlEncoded
    @GET("api/wisata/4")
    suspend fun getWistaDetail(): String

    @FormUrlEncoded
    @GET("api/wisata?search=jawa")
    suspend fun getWistaSearch(): String

    @FormUrlEncoded
    @GET("api/image")
    suspend fun getWistaImage(): String

    @FormUrlEncoded
    @GET("api/bookmark-list")
    suspend fun getBookmarkList(): String

    @FormUrlEncoded
    @POST("api/bookmark-list")
    suspend fun getWisataBookmark(): String

    @FormUrlEncoded
    @POST("api/wisata/4/unbookmark")
    suspend fun getWisataUnbookmark(): String

    @FormUrlEncoded
    @GET("api/category-list")
    suspend fun getCategoryList(): String

    @FormUrlEncoded
    @GET("api/view-category/3")
    suspend fun getCategoryView(): String

*/




