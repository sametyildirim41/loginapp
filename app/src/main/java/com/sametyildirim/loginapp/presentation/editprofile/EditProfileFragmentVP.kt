package com.sametyildirim.loginapp.presentation.editprofile

import android.os.Bundle
import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.presentation.base.BaseFragment

interface EditProfileFragmentVP {

    interface View{
        fun setFragment(baseFragment: BaseFragment, bundle: Bundle?)
        fun setError(error: String)
        fun setItem(item: User)
        fun showProgress()
        fun hideProgress()
    }

    interface Presenter{
        fun updateUser(user_Req: User, data : ByteArray)
        fun navigateToMainFragment(bundle: Bundle?)
        fun onDestroy()
    }
}