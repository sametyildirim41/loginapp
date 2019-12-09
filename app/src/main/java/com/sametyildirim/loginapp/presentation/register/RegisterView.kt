package com.sametyildirim.loginapp.presentation.register

import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User

interface RegisterView {

    fun navigateToLogin()
    fun setError(error: String)
    fun setItem(item: User)
    fun onGetRealmSuccess(dbProperties: DbProperties)
    fun onSetRealmSuccess()
    fun showProgress()
    fun hideProgress()
}