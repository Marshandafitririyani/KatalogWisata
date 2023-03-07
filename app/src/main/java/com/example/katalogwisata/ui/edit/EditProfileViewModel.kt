package com.example.katalogwisata.ui.edit

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toObject
import com.example.katalogwisata.api.ApiService
import com.example.katalogwisata.data.base.BaseViewModel
import com.example.katalogwisata.data.session.Session
import com.example.katalogwisata.data.user.User
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
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
                session.saveUser(data)
                if (status == ApiCode.SUCCESS) {

                }
            }
            override suspend fun onError(response: ApiResponse) {
                super.onError(response)
                _apiResponse.send(ApiResponse().responseError())
            }
        })
    }

    fun userUpdateWithPhoto(method: String,name: String, phoneNumber: String, photo: File) = viewModelScope.launch {
        val fileBody = photo.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val filePart = MultipartBody.Part.createFormData("image", photo.name, fileBody)
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
                    _apiResponse.send(ApiResponse().responseError())
                }


            })
    }
}




