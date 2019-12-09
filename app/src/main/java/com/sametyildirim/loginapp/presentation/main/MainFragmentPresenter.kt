package com.sametyildirim.loginapp.presentation.main

import android.app.Activity
import android.os.Bundle
import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.helper.DialogTwoButtons
import com.sametyildirim.loginapp.presentation.DbPropertiesInteractor
import com.sametyildirim.loginapp.presentation.base.BaseFragment
import com.sametyildirim.loginapp.presentation.base.BaseInteractor
import com.sametyildirim.loginapp.presentation.base.FragmentNavigation
import com.sametyildirim.loginapp.presentation.editprofile.EditProfileFragment

class MainFragmentPresenter (var view: MainFragmentVP.View?) : MainFragmentVP.Presenter, FragmentNavigation.Presenter , BaseInteractor.OnFinishedListener,
    DialogTwoButtons.OnClickDialogButtonListener, DbPropertiesInteractor.OnFinishedListener

{
    private val dbPropertiesInteractor = DbPropertiesInteractor()
    private val baseInteractor = BaseInteractor()

    override fun addFragment(fragment: BaseFragment) {
        view?.setFragment(fragment, null)
    }

    override fun navigateToEditProfileFragment(bundle: Bundle?) {
        view?.setFragment(EditProfileFragment(),bundle)
    }

    override fun deleteUser(user_Req: User) {
//        view?.showProgress()
        baseInteractor.executeDeleteUser(user_Req , this )
    }

    override fun onDestroy() {
        view = null
    }

    override fun onSuccess(item: User) {
        view?.apply {
            val mDbProperties = DbProperties()
            mDbProperties.rememberMe = "unchecked"
            mDbProperties.username = ""
            mDbProperties.photoPath = ""
            dbPropertiesInteractor.executeSetDbProperties("OK",mDbProperties,this@MainFragmentPresenter)
//            hideProgress()
        }
    }

    override fun onSetRealmSuccess(loginState: String?) {
        view?.navigateToLogin()
    }

    override fun onGetRealmSuccess(dbProperties: DbProperties) {}

    override fun onError(error: String) {
        view?.apply {
            setError(error)
//            hideProgress()
        }
    }

    override fun onClickLeftButton(dialogCode: String) {
    }

    override fun onClickRightButton(dialogCode : String) {
        view?.executeDeleteUser()
    }

    override fun dialogDeleteUser(activity: Activity, dialogCode: String, title: String, message: String, leftButton: String, rightButton: String, cancelButton: String) {

        val dialogDeleteUser = DialogTwoButtons(activity)
        dialogDeleteUser.createDialog(dialogCode,title,message,leftButton,rightButton,cancelButton,this)

    }

}