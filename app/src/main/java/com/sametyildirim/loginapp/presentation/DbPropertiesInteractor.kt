package com.sametyildirim.loginapp.presentation

import android.os.Handler

import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.realmdatabase.DbPropertiesFields
import io.realm.Realm
import io.realm.kotlin.where

class DbPropertiesInteractor {

    interface OnFinishedListener{
        fun onGetRealmSuccess(dbProperties: DbProperties)
        fun onSetRealmSuccess(loginState: String?)
    }


    fun executeGetDbProperties( listener: OnFinishedListener  ) {

        Handler().postDelayed({
            getDbProperties(listener)
        }, 0)

    }

    private fun getDbProperties( listener: OnFinishedListener ){

        val realm : Realm = Realm.getDefaultInstance()
        var username :String?
        var name :String?
        var surname :String?
        var photoUrl :String?
        var rememberMe :String?

        realm.executeTransaction {

                val value: Int? = 1
                val result = realm.where<DbProperties>().equalTo(DbPropertiesFields.ID, value).findFirst()

                if (result != null) {
                    username = result.username
                    photoUrl = result.photoPath
                    rememberMe = result.rememberMe
                    name = result.name
                    surname = result.surname

                } else {
                    username = ""
                    photoUrl = ""
                    rememberMe = ""
                    name = ""
                    surname = ""
                }

                val dbProperties = DbProperties(1,username,name,surname,photoUrl,rememberMe)

                listener.onGetRealmSuccess(dbProperties)
        }

    }


    fun executeSetDbProperties(loginState: String?, dbProperties: DbProperties, listener: OnFinishedListener  ) {

        Handler().postDelayed({
            setDbProperties(loginState, dbProperties,listener)
        }, 0)

    }

    private fun setDbProperties(loginState: String?, dbProperties: DbProperties, listener: OnFinishedListener){

        val realm : Realm = Realm.getDefaultInstance()
        val value : Int? = 1

        realm.executeTransaction {
            var result = realm.where<DbProperties>().equalTo(DbPropertiesFields.ID, value).findFirst()
            if (result == null) {
                result = realm.createObject(DbProperties::class.java, 1)
                if(dbProperties.username != null)
                    result.username = dbProperties.username
                if(dbProperties.photoPath != null)
                    result.photoPath = dbProperties.photoPath
                if(dbProperties.rememberMe != null)
                    result.rememberMe = dbProperties.rememberMe
                if(dbProperties.name != null)
                    result.name= dbProperties.name
                if(dbProperties.surname != null)
                    result.surname = dbProperties.surname

            }else {
                if(dbProperties.username != null)
                    result.username = dbProperties.username
                if(dbProperties.photoPath != null)
                    result.photoPath = dbProperties.photoPath
                if(dbProperties.rememberMe != null)
                    result.rememberMe = dbProperties.rememberMe
                if(dbProperties.name != null)
                    result.name= dbProperties.name
                if(dbProperties.surname != null)
                    result.surname = dbProperties.surname
            }

            listener.onSetRealmSuccess(loginState)

        }
    }
}