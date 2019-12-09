package com.sametyildirim.loginapp.domain.model.realmdatabase

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class DbProperties (

        @PrimaryKey
        var id: Int = 1,

        var username: String? = null,

        var name: String? = null,

        var surname: String? = null,

        var photoPath: String? = "",

        var rememberMe : String? = "unchecked"

) : RealmObject()
