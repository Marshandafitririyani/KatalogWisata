package com.example.katalogwisata.data.base

import androidx.databinding.ViewDataBinding
import com.crocodic.core.base.activity.CoreActivity
import com.crocodic.core.base.viewmodel.CoreViewModel
import com.crocodic.core.extension.clearNotification
import com.crocodic.core.extension.openActivity
import com.example.katalogwisata.data.user.Session
import com.example.katalogwisata.ui.enter.EnterActivity
import javax.inject.Inject

open class BaseActivity<VB : ViewDataBinding, VM : CoreViewModel>(layoutRes: Int) : CoreActivity<VB, VM>(layoutRes) {

    @Inject
    lateinit var session: Session

    //supaya tidak login lagi
    override fun authLogoutSuccess() {
        super.authLogoutSuccess()
        loadingDialog.dismiss()
        expiredDialog.dismiss()
        clearNotification()
        session.clearAll()
        openActivity<EnterActivity>()
        finishAffinity()
    }
}