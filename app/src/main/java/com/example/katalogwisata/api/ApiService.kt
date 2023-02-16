package com.example.katalogwisata.api

import okhttp3.MultipartBody
import retrofit2.http.*
import java.io.File

interface ApiService {
    @FormUrlEncoded
    @POST("api/login")
    suspend fun login(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): String

    @FormUrlEncoded
    @POST("api/register")
    suspend fun register(
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): String

    @FormUrlEncoded
    @POST("api/member/2")
    suspend fun updateProfile(
        @Field("_method") method: String?,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("password") password: String?
    ): String

    @Multipart
    @PATCH("api/member/2")
    suspend fun updateProfileWithPhoto(
        @Part("_method") method: String?,
        @Part("name") name: String?,
        @Part image: MultipartBody.Part?,
        @Part("email") email: String?,
        @Part("password") password: String?
    ): String

    @POST("api/logout")
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










}

