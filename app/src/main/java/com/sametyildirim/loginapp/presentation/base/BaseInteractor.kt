package com.sametyildirim.loginapp.presentation.base

import android.os.Handler
import android.util.Log
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.sametyildirim.loginapp.domain.model.response.User

class BaseInteractor {

    private lateinit var database: DatabaseReference
    private lateinit var postReference: DatabaseReference
     companion object {
         private var profileName = String()
         private var profileEmail = String()
         private var photoPath = String()
     }

    interface OnFinishedListener{
        fun onSuccess(item: User)
        fun onError(error: String)
    }

    fun executeGetUserCheck(user: User, listener: OnFinishedListener) {

            Handler().postDelayed({
                getUserCheck(user, listener)
            }, 1000)
    }

    //********************
    //**  Bilgilendirme : User işlemlerini Google Firebase Auth. kullanarak basitçe yapabilirdim. Farklılık olması açısından normal db sini kullanarak yaptım.
    // Tabiki Google Firebase Auth. kullanılması güvenlik açısından önemli. Ben bilinçli olarak test taskı olduğu için farklılık olması açısından bu şekilde yaptım. **
    //********************

    private fun getUserCheck(user: User, listener: OnFinishedListener){

        postReference = FirebaseDatabase.getInstance().reference
            .child("users")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.children.find { it.child("username").value == user.username }?.getValue(User::class.java)?.password == user.password)
                {
                    val mUser = dataSnapshot.children.find { it.getValue(User::class.java)!!.username == user.username }!!.getValue(User::class.java)!!
                    listener.onSuccess(mUser)
                    profileName = mUser.name.toString().toLowerCase().capitalize() + " " + mUser.surname.toString().toUpperCase()
                    profileEmail = user.username.toString()
                    photoPath = mUser.photoPath.toString()
                }
                else
                    listener.onError("UsernameOrPasswordIsIncorrect")
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }
        postReference.addListenerForSingleValueEvent(postListener)

    }

    fun executeNewUser(user: User, data : ByteArray, listener: OnFinishedListener) {

        Handler().postDelayed({
            saveNewUser(user , data,  listener)
        }, 1000)
    }

    private fun saveNewUser(user: User , data : ByteArray ,listener: OnFinishedListener) {
        database = FirebaseDatabase.getInstance().reference

        postReference = FirebaseDatabase.getInstance().reference
            .child("users")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.children.find { it.child("username").value == user.username } == null)
                {
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val profileRef = storageRef.child(user.username + ".jpg")
                    val uploadTask = profileRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        Log.d("PhotoUpload ","Failure")
                    }.addOnSuccessListener {
                        Log.d("PhotoUpload ","Success")
                        it.storage.downloadUrl.addOnCompleteListener {
                                aa -> aa.addOnSuccessListener{
                                bb -> user.photoPath = bb.toString()
                            database.child("users").push().setValue(user)
                                .addOnSuccessListener {
                                    Log.d("SaveUser ","Success")
                                    listener.onSuccess(user) }
                                .addOnFailureListener {
                                    Log.d("SaveUser ","Failure")
                                    listener.onError("ConnectionError") }
                        } }
                    }
                }
                else
                    listener.onError("UsernameAlreadyTaken")
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
            postReference.addListenerForSingleValueEvent(postListener)
    }

    fun executeUpdateUser(user: User, data : ByteArray, listener: OnFinishedListener) {

        Handler().postDelayed({
            updateUser(user , data,  listener)
        }, 1000)
    }

    private fun updateUser(user: User , data : ByteArray ,listener: OnFinishedListener) {
        database = FirebaseDatabase.getInstance().reference

        postReference = FirebaseDatabase.getInstance().reference
            .child("users")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.children.find { it.child("username").value == user.username } != null)
                {
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val profileRef = storageRef.child(user.username + ".jpg")
                    val uploadTask = profileRef.putBytes(data)
                    uploadTask.addOnFailureListener {
                        Log.d("PhotoUpload ","Failure")
                    }.addOnSuccessListener {
                        Log.d("PhotoUpload ","Success")
                        it.storage.downloadUrl.addOnCompleteListener {
                                aa -> aa.addOnSuccessListener{
                                bb -> user.photoPath = bb.toString()
                            database.child("users").child(dataSnapshot.children.find { it.child("username").value == user.username }?.key!!).setValue(user)
                                .addOnSuccessListener {
                                    profileName = user.name.toString().toLowerCase().capitalize() + " " + user.surname.toString().toUpperCase()
                                    profileEmail = user.username.toString()
                                    photoPath = user.photoPath.toString()
                                    Log.d("UpdateUser ","Success")
                                    listener.onSuccess(user) }
                                .addOnFailureListener {
                                    Log.d("UpdateUser ","Failure")
                                    listener.onError("ConnectionError") }
                        } }
                    }
                }
                else
                    listener.onError("UserNotFound")
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        postReference.addListenerForSingleValueEvent(postListener)
    }

    fun executeDeleteUser(user: User, listener: OnFinishedListener) {

        Handler().postDelayed({
            deleteUser(user , listener)
        }, 1000)
    }

    private fun deleteUser(user: User ,listener: OnFinishedListener) {
        database = FirebaseDatabase.getInstance().reference

        postReference = FirebaseDatabase.getInstance().reference
            .child("users")

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                if(dataSnapshot.children.find { it.child("username").value == user.username } != null)
                {
                    val storage = FirebaseStorage.getInstance()
                    val storageRef = storage.reference
                    val profileRef = storageRef.child(user.username + ".jpg")

                    database.child("users").child(dataSnapshot.children.find { it.child("username").value == user.username }?.key!!).removeValue()
                        .addOnSuccessListener {
                            profileRef.delete()
                                .addOnSuccessListener { Log.d("PhotoDelete ", "Success") }
                                .addOnFailureListener { Log.d("PhotoDelete ", "Failure") }
                            Log.d("UserDelete ", "Success")
                            listener.onSuccess(user)
                        }
                        .addOnFailureListener {
                            Log.d("UserDelete ", "Failure")
                            listener.onError("ConnectionError") }
                }
                else
                    listener.onError("UserNotFound")
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        }
        postReference.addListenerForSingleValueEvent(postListener)
    }

    fun getUserInfo( callback: (HashMap<String,String>) -> Unit) {

        Handler().postDelayed({
            callback(userInfo())
        }, 2000)

    }

    private fun userInfo(): HashMap<String,String>
    {
        val userInfo = HashMap<String,String>()
        userInfo["profileName"] = profileName
        userInfo["profileEmail"] = profileEmail
        userInfo["photoPath"] = photoPath
        return userInfo
    }

}