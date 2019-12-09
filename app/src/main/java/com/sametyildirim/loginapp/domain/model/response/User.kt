package com.sametyildirim.loginapp.domain.model.response

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    var username: String? = "",
    var name: String? = "",
    var surname: String? = "",
    var password: String? = "",
    var photoPath: String? = ""
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "username" to username,
            "name" to name,
            "surname" to surname,
            "password" to password,
            "photoPath" to password
        )
    }
}