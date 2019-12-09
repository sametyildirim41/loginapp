package com.sametyildirim.loginapp.presentation.login


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.sametyildirim.loginapp.BuildConfig
import com.sametyildirim.loginapp.R
import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.helper.ToastNotice
import com.sametyildirim.loginapp.presentation.DbPropertiesInteractor
import com.sametyildirim.loginapp.presentation.base.BaseActivity
import com.sametyildirim.loginapp.presentation.base.BaseInteractor
import com.sametyildirim.loginapp.presentation.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import java.io.File


class LoginActivity : AppCompatActivity() , LoginView {

    private val presenter = LoginPresenter(this, BaseInteractor(), DbPropertiesInteractor())
    private var photoPath : String? = ""
    private var currentUser = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        presenter.getDbProperties()

        btnLogin.setOnClickListener { validateCredentials() }
        btnLogin.isAllCaps = true
        txtVersion.text = (getString(R.string.version) + " " + BuildConfig.VERSION_NAME + " ")

        btnRegister.setOnClickListener {
            val myIntent = Intent(this, RegisterActivity::class.java)
            myIntent.putExtra("username", edtUserName.text.toString())
            startActivity(myIntent)
        }

        if(!intent.getStringExtra("username").isNullOrEmpty()){
            edtUserName.setText(intent.getStringExtra("username"))
            if(!intent.getStringExtra("photoPath").isNullOrEmpty()){
                photoPath = intent.getStringExtra("photoPath")
                Glide.with(this).load(photoPath).into(imgProfilePhoto)
            }
        }

    }

    override fun onGetRealmSuccess(dbProperties: DbProperties) {

        if(dbProperties.rememberMe == "checked")
        {
            switchRememberMe.isChecked = true
            if(!dbProperties.username.isNullOrEmpty())
            { edtUserName.setText(dbProperties.username!!) }
            if(!dbProperties.photoPath.isNullOrEmpty())
            {
                photoPath = dbProperties.photoPath
                Glide.with(this).load(photoPath).into(imgProfilePhoto)
            }
        }
        else
            switchRememberMe.isChecked = false

    }

    private fun validateCredentials(){

        if(!edtUserName.text!!.isBlank()) {
            if (isEmailValid(edtUserName.text!!.toString())) {
                edtUserName.error = null
                edtUserName.clearFocus()
            if (!edtPassword.text!!.isBlank()) {
                edtPassword.error = null
                edtPassword.clearFocus()
                val mUserReq = User()
                mUserReq.username = edtUserName.text.toString()
                mUserReq.password = edtPassword.text.toString()
                presenter.validateCredentials(mUserReq)
            } else {
                edtPassword.error = getString(R.string.pleaseEnterPassword)
                edtPassword.requestFocus()
                YoYo.with(Techniques.Shake)
                        .duration(700)
                        .repeat(1)
                        .playOn(txtPasswordLayout)
            }
        } else {
            edtUserName.error = getString(R.string.invalidUsername)
            edtUserName.requestFocus()
            YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(1)
                .playOn(txtUserNameLayout)
        }
    }
    else {
        edtUserName.error = getString(R.string.pleaseEnterUsername)
        edtUserName.requestFocus()
        YoYo.with(Techniques.Shake)
            .duration(700)
            .repeat(1)
            .playOn(txtUserNameLayout)
    }
    }

    override fun setItem(item: User) {

        val mDbProperties = DbProperties()
        photoPath = item.photoPath
        if(switchRememberMe.isChecked){
            mDbProperties.rememberMe = "checked"
            mDbProperties.username = edtUserName.text.toString()
            mDbProperties.photoPath = photoPath
        }
        else{
            mDbProperties.rememberMe = "unchecked"
            mDbProperties.username = ""
            mDbProperties.photoPath = ""
        }

        Log.d("Realm ","Set Item")

        if(!photoPath.isNullOrEmpty()){
            Glide.with(this).load(photoPath).into(imgProfilePhoto)
        }

        presenter.setDbProperties("OK",mDbProperties)
        currentUser = item

    }

    override fun onSetRealmSuccess() {
        Log.d("Realm ","Success")
        navigateToMain()
    }

    override fun navigateToMain() {
        val myIntent = Intent(this, BaseActivity::class.java)
        myIntent.putExtra("username", currentUser.username)
        myIntent.putExtra("photoPath", currentUser.photoPath)
        myIntent.putExtra("name", currentUser.name)
        myIntent.putExtra("surname", currentUser.surname)
        startActivity(myIntent)
    }

    override fun showProgress() {
        loginLayout.isEnabled = false
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        loginLayout.isEnabled = true
        progress.visibility = View.GONE
    }

    override fun setError(error: String) {
        if(error == "UsernameOrPasswordIsIncorrect")
            ToastNotice(this,getString(R.string.usernameOrPasswordIsIncorrect), ToastNotice.ToastType.WARNING)
        else
            ToastNotice(this,error, ToastNotice.ToastType.WARNING)
    }

//    private fun usernameFromEmail(email: String): String {
//        return if (email.contains("@")) {
//            email.split("@".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
//        } else {
//            email
//        }
//    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}