package com.example.katalogwisata.ui.save

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.extension.toList
import com.example.katalogwisata.api.ApiService
import com.example.katalogwisata.data.base.BaseViewModel
import com.example.katalogwisata.data.session.Session
import com.example.katalogwisata.data.user.Tour
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class SaveTourViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session,

    ): BaseViewModel() {
    var tour = MutableLiveData<List<Tour>>()

    fun touList() = viewModelScope.launch {
        ApiObserver({ apiService.tourList()},false,object : ApiObserver.ResponseListener{
            override suspend fun onSuccess(response: JSONObject) {
                val status = response.getInt(ApiCode.STATUS)
                if (status == ApiCode.SUCCESS) {

                    val data = response.getJSONArray(ApiCode.DATA).toList<Tour>(gson)

                    tour.postValue(data)
                    _apiResponse.send(ApiResponse().responseSuccess())
                }
            }
        })
    }
}