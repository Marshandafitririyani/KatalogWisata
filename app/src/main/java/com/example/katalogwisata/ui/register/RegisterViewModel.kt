package com.example.katalogwisata.ui.register

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.example.katalogwisata.api.ApiService
import com.example.katalogwisata.data.base.BaseViewModel
import com.google.gson.Gson
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel
@Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson
) :

    BaseViewModel() {
    fun register(name: String, email: String, password: String) = viewModelScope.launch {
        _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver(
            { apiService.register(name, email, password) },
            false,
            object : ApiObserver.ResponseListener {
                override suspend fun onSuccess(response: JSONObject) {
                    _apiResponse.send(ApiResponse().responseSuccess())
                }

            })
    }
}
