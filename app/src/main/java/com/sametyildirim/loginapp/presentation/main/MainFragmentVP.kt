package com.sametyildirim.loginapp.presentation.main

import android.app.Activity
import android.os.Bundle
import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.presentation.base.BaseFragment

interface MainFragmentVP {

    interface View{
        fun setFragment(baseFragment: BaseFragment, bundle: Bundle?)
        fun setError(error: String)
        fun executeDeleteUser()
        fun navigateToLogin()
    }

    interface Presenter{
        fun navigateToEditProfileFragment(bundle: Bundle?)
        fun dialogDeleteUser(activity: Activity, dialogCode: String, title: String, message: String, leftButton: String, rightButton: String, cancelButton: String)
        fun deleteUser(user_Req: User)
        fun onDestroy()
    }
}