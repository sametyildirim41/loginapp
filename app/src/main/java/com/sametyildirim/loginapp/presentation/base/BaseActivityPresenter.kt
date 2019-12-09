package com.sametyildirim.loginapp.presentation.base

import android.app.Activity
import android.os.Bundle
import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.helper.DialogTwoButtons
import com.sametyildirim.loginapp.presentation.DbPropertiesInteractor
import com.sametyildirim.loginapp.presentation.editprofile.EditProfileFragment
import com.sametyildirim.loginapp.presentation.main.MainFragment

class BaseActivityPresenter(val view: BaseActivityVP.View,private val baseInteractor: BaseInteractor)
    : BaseActivityVP.Presenter, FragmentNavigation.Presenter, DialogTwoButtons.OnClickDialogButtonListener{

    override fun addFragment(fragment: BaseFragment) {
       view.setFragment(fragment,null)
    }

    override fun navigateToMainFragment(bundle: Bundle?) {
        view.setFragment(MainFragment(),bundle)
    }

    override fun navigateToEditProfileFragment(bundle: Bundle?) {
        view.setFragment(EditProfileFragment(),bundle)
    }

    override fun onClickLeftButton(dialogCode: String) {
    }

    override fun onClickRightButton(dialogCode : String) {
        view.navigateToLogin()
    }

    override fun logout(activity: Activity, dialogCode: String, title: String, message: String, leftButton: String, rightButton: String, cancelButton: String) {

        val dialogLogout = DialogTwoButtons(activity)
        dialogLogout.createDialog(dialogCode,title,message,leftButton,rightButton,cancelButton,this)

    }

    override fun getProfileInfo(){
        baseInteractor.getUserInfo {
            view.apply { profileInfo(it["profileName"]!!.toString(),it["profileEmail"]!!.toString(),it["photoPath"]!!.toString()) }
        }
    }

}