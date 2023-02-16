package com.example.katalogwisata.ui.edit

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.data.CoreSession
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
import retrofit2.http.PUT
import timber.log.Timber
import java.io.File
import javax.inject.Inject

@HiltViewModel
class EditProfilViewModel @Inject constructor(
    private val apiService: ApiService,
    private val session: Session,
    private val gson: Gson,
) : BaseViewModel() {
    fun updateProfile(name: String, email: String) = viewModelScope.launch {
        Timber.d("Cek 1")
//   _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver({
            apiService.updateProfile(
                "PUT",
                name,
                email,
                "haysan12345678"
            )
        }, false, object : ApiObserver.ResponseListener {
            override suspend fun onSuccess(response: JSONObject) {
                val status = response.getInt(ApiCode.STATUS)
                Timber.d("Cek 2")
                if (status == ApiCode.SUCCESS) {
                    val message = response.getString(ApiCode.MESSAGE)
                    Timber.d("Cek 3")
//               _apiResponse.send(ApiResponse(status = ApiStatus.SUCCESS, message = message ))
                }
            }
        })
    }

    //untuk update profil photo
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
            })
    }

}