package com.example.katalogwisata.ui.edit

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.extension.toObject
import com.example.katalogwisata.api.ApiService
import com.example.katalogwisata.data.base.BaseViewModel
import com.example.katalogwisata.data.user.Session
import com.example.katalogwisata.data.user.User
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: Session,
    private val gson: Gson,

) : BaseViewModel() {
    fun userUpdate(method: String, name: String, phoneNumber: String) = viewModelScope.launch {
        ApiObserver({
            apiService.userUpdate(method, name, phoneNumber,)
        }, false, object : ApiObserver.ResponseListener {
            override suspend fun onSuccess(response: JSONObject) {
                val status = response.getInt(ApiCode.STATUS)
                val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                _apiResponse.send(ApiResponse().responseSuccess("Profile Updated"))
                session.saveUser(data) //untuk menyimpan username yg telah di update
                if (status == ApiCode.SUCCESS) {

                }
            }

            override suspend fun onError(response: ApiResponse) {
                super.onError(response)
                _apiResponse.send(ApiResponse().responseError())
            }
        })
    }
    //untuk update profil photo
    fun userUpdateWithPhoto(method: String,name: String, phoneNumber: String, photo: File) = viewModelScope.launch {
        println("Nama: $name")
        val fileBody = photo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("photo", photo.name, fileBody)
        _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.userUpdateWithPhoto(method, name, phoneNumber, filePart,) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {

                    val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                    _apiResponse.send(ApiResponse().responseSuccess("profile updated"))
                    session.saveUser(data)

                }
                override suspend fun onError(response: ApiResponse) {
                    super.onError(response)
                    Log.d("cek photo",response.toString())
                    _apiResponse.send(ApiResponse().responseError())
                }


            })
    }
}

   /* //untuk update profil photo
    fun updateProfileWithPhoto(name: String, email: String, photo: File) = viewModelScope.launch {
        println("Nama: $name")
        val fileBody = photo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("photo", photo.name, fileBody)
        _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver(
            {
                apiService.updateProfile(
                    "PUT",
                    name,
                    email,
                    "haysan12345678"
                )
            },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                    _apiResponse.send(ApiResponse().responseSuccess("profile updated"))

                }
            })*/


