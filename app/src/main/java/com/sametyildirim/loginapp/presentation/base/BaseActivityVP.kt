package com.sametyildirim.loginapp.presentation.base

import android.app.Activity
import android.os.Bundle

interface BaseActivityVP {

    interface View{
        fun setFragment(baseFragment: BaseFragment, bundle: Bundle?)
        fun navigateToLogin()
        fun profileInfo(profileName: String, profileEmail: String, photoPath: String?)
    }

    interface Presenter{
        fun navigateToMainFragment(bundle: Bundle?)
        fun navigateToEditProfileFragment(bundle: Bundle?)
        fun logout(activity: Activity, dialogCode: String, title: String, message: String, leftButton: String, rightButton: String, cancelButton: String)
        fun getProfileInfo()
    }
}