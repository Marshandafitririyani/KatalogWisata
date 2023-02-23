package com.example.katalogwisata.ui.profile

import androidx.lifecycle.viewModelScope
import com.crocodic.core.api.ApiObserver
import com.example.katalogwisata.api.ApiService
import com.example.katalogwisata.data.base.BaseViewModel
import com.example.katalogwisata.data.user.Session
import com.example.katalogwisata.data.user.User
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val gson: Gson,
    private val session: Session,

    ): BaseViewModel() {

    private val _user = kotlinx.coroutines.channels.Channel<List<User>>()
    val user = _user.receiveAsFlow()

    val getUser = session.getUser()

    //fungsi logout
    fun logout() = viewModelScope.launch {
        ApiObserver({apiService.logout()},
        false, object : ApiObserver.ResponseListener{
                override suspend fun onSuccess(response: JSONObject) {
                    session.clearAll()
                }
        })
    }
    /*fun  logout(logout: () -> Unit) = viewModelScope.launch {
        logout()
        logoutSuccess()
    }*/
}

//fun logout() = viewModelScope.launch {
//    ApiObserver({ apiService.logout() },
//        false, object : ApiObserver.ResponseListener {
//            override suspend fun onSuccess(response: JSONObject) {
//                session.clearAll()
//            }
//        })
//}