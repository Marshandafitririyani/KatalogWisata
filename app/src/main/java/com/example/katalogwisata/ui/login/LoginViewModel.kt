package com.example.katalogwisata.ui.login

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiCode
import com.crocodic.core.api.ApiObserver
import com.crocodic.core.api.ApiResponse
import com.crocodic.core.api.ApiStatus
import com.crocodic.core.data.CoreSession
import com.crocodic.core.extension.toObject
import com.example.katalogwisata.api.ApiService
import com.example.katalogwisata.data.base.BaseViewModel
import com.example.katalogwisata.data.const.Const
import com.example.katalogwisata.data.user.Session
import com.example.katalogwisata.data.user.User
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session

    ): BaseViewModel() {
    fun login(email: String, password: String) = viewModelScope.launch {
        _apiResponse.send(ApiResponse().responseLoading())
        ApiObserver({apiService.login(email, password)}, false, object : ApiObserver.ResponseListener{
            override suspend fun  onSuccess(response: JSONObject) {
                val status = response.getInt(ApiCode.STATUS)
                val data = response.getJSONObject(ApiCode.DATA).toObject<User>(gson)
                val newToken = response.getString("access_token")
                session.setValue(Const.TOKEN.API_TOKEN, newToken)
                session.saveUser(data)
                if (status == ApiCode.SUCCESS){
                    val message = response.getString(ApiCode.MESSAGE)
                    _apiResponse.send(ApiResponse(status = ApiStatus.SUCCESS, message = message ))
                }
            }
        })
    }

}



