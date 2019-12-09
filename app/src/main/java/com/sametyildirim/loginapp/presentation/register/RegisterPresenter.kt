package com.sametyildirim.loginapp.presentation.register

import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.presentation.DbPropertiesInteractor
import com.sametyildirim.loginapp.presentation.base.BaseInteractor

class RegisterPresenter(private var registerView: RegisterView?,
                        private val baseInteractor: BaseInteractor,
                        private val dbPropertiesInteractor: DbPropertiesInteractor
) :
                     BaseInteractor.OnFinishedListener,DbPropertiesInteractor.OnFinishedListener {


    fun validateCredentials(user_Req: User, data : ByteArray) {
        registerView?.showProgress()
        baseInteractor.executeNewUser(user_Req, data , this )
    }

    fun getDbProperties(){
        dbPropertiesInteractor.executeGetDbProperties(this)
    }

    fun setDbProperties(loginState: String?, dbProperties: DbProperties){
        dbPropertiesInteractor.executeSetDbProperties(loginState,dbProperties,this)
    }

    fun onDestroy() {
        registerView = null
    }

    override fun onSuccess(item: User) {
        registerView?.apply {
            setItem(item)
            hideProgress()
        }
    }

    override fun onError(error: String) {
        registerView?.apply {
            setError(error)
            hideProgress()
        }
    }

    override fun onGetRealmSuccess(dbProperties: DbProperties) {
        registerView?.apply {
          onGetRealmSuccess(dbProperties)
        }
    }

    override fun onSetRealmSuccess(loginState: String?) {
        registerView?.apply {
            when(loginState)
            {
                "OK" -> onSetRealmSuccess()
            }
        }
    }



}