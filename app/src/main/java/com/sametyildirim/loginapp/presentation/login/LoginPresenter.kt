package com.sametyildirim.loginapp.presentation.login

import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.presentation.DbPropertiesInteractor
import com.sametyildirim.loginapp.presentation.base.BaseInteractor

class LoginPresenter(private var loginView: LoginView?,
                     private val baseInteractor: BaseInteractor,
                     private val dbPropertiesInteractor: DbPropertiesInteractor
) :
                     BaseInteractor.OnFinishedListener,DbPropertiesInteractor.OnFinishedListener {

    fun validateCredentials(user_Req: User) {
        loginView?.showProgress()
        baseInteractor.executeGetUserCheck(user_Req, this )
    }

    fun getDbProperties(){
        dbPropertiesInteractor.executeGetDbProperties(this)
    }

    fun setDbProperties(loginState: String?, dbProperties: DbProperties){
        dbPropertiesInteractor.executeSetDbProperties(loginState,dbProperties,this)
    }

    fun onDestroy() {
        loginView = null
    }

    override fun onSuccess(item: User) {
        loginView?.apply {
            setItem(item)
            hideProgress()
        }
    }

    override fun onError(error: String) {
        loginView?.apply {
            setError(error)
            hideProgress()
        }
    }

    override fun onGetRealmSuccess(dbProperties: DbProperties) {
        loginView?.apply {
          onGetRealmSuccess(dbProperties)
        }
    }

    override fun onSetRealmSuccess(loginState: String?) {
        loginView?.apply {
            when(loginState)
            {
                "OK" -> onSetRealmSuccess()
            }
        }
    }



}