package com.sametyildirim.loginapp.presentation.editprofile

import android.os.Bundle
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.presentation.base.BaseFragment
import com.sametyildirim.loginapp.presentation.base.BaseInteractor
import com.sametyildirim.loginapp.presentation.base.FragmentNavigation
import com.sametyildirim.loginapp.presentation.main.MainFragment

class EditProfileFragmentPresenter(var view: EditProfileFragmentVP.View?) : EditProfileFragmentVP.Presenter, FragmentNavigation.Presenter
    ,BaseInteractor.OnFinishedListener {

    private val baseInteractor = BaseInteractor()

    override fun addFragment(fragment: BaseFragment) {
        view?.setFragment(fragment, null)
    }

    override fun navigateToMainFragment(bundle: Bundle?) {
        view?.setFragment(MainFragment(),bundle)
    }

    override fun updateUser(user_Req: User, data : ByteArray) {
        view?.showProgress()
        baseInteractor.executeUpdateUser(user_Req, data , this )
    }

    override fun onDestroy() {
        view = null
    }

    override fun onSuccess(item: User) {
        view?.apply {
            setItem(item)
            hideProgress()
        }
    }

    override fun onError(error: String) {
        view?.apply {
            setError(error)
            hideProgress()
        }
    }
}