package com.sametyildirim.loginapp.presentation.login

import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User

interface LoginView {

    fun navigateToMain()
    fun setError(error: String)
    fun setItem(item: User)
    fun onGetRealmSuccess(dbProperties: DbProperties)
    fun onSetRealmSuccess()
    fun showProgress()
    fun hideProgress()
}