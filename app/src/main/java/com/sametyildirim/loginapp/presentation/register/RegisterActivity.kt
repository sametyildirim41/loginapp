package com.sametyildirim.loginapp.presentation.register


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sametyildirim.loginapp.R
import com.sametyildirim.loginapp.domain.model.realmdatabase.DbProperties
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.helper.ToastNotice
import com.sametyildirim.loginapp.presentation.DbPropertiesInteractor
import com.sametyildirim.loginapp.presentation.base.BaseInteractor
import com.sametyildirim.loginapp.presentation.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class RegisterActivity : AppCompatActivity() , RegisterView {

    private val presenter = RegisterPresenter(this, BaseInteractor(), DbPropertiesInteractor())
    private val TAKE_PHOTO_REQUEST = 101
    private var mCurrentPhotoPath: String = ""
    private var photoPath: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        presenter.getDbProperties()

        btnRegister.isAllCaps = true

        val username = intent.getStringExtra("username")

        edtUserName.setText(username)

        btnRegister.setOnClickListener {

            validateCredentials()

        }

        imgProfilePhoto.setOnClickListener {
            if(validatePermissions()){
                    launchCamera()
            }
        }

    }

    override fun onGetRealmSuccess(dbProperties: DbProperties) {

    }

    private fun validateCredentials(){

        if(!edtUserName.text!!.isBlank()) {
            if (isEmailValid(edtUserName.text!!.toString())) {
                edtUserName.error = null
                edtUserName.clearFocus()
                if (!edtPassword.text!!.isBlank()) {
                    edtPassword.error = null
                    edtPassword.clearFocus()
                    if (!edtPasswordAgain.text!!.isBlank()) {
                        edtPasswordAgain.error = null
                        edtPasswordAgain.clearFocus()
                        if (edtPassword.text.toString() == (edtPasswordAgain.text.toString())) {

                            imgProfilePhoto.isDrawingCacheEnabled = true
                            imgProfilePhoto.buildDrawingCache()
                            val bitmap = (imgProfilePhoto.drawable as BitmapDrawable).bitmap
                            Bitmap.createScaledBitmap(bitmap, 50, 50, false)
                            val baos = ByteArrayOutputStream()
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos)
                            val data = baos.toByteArray()

                            val mUserReq = User()
                            mUserReq.username = edtUserName.text.toString()
                            mUserReq.password = edtPassword.text.toString()
                            mUserReq.name = edtName.text.toString()
                            mUserReq.surname = edtSurName.text.toString()
                            presenter.validateCredentials(mUserReq, data)

                        } else {
                            edtPassword.setText("")
                            edtPasswordAgain.setText("")
                            edtPassword.error = getString(R.string.passwordsDoNotMatch)
                            edtPassword.requestFocus()
                            YoYo.with(Techniques.Shake)
                                .duration(700)
                                .repeat(1)
                                .playOn(txtPasswordLayout)
                            YoYo.with(Techniques.Shake)
                                .duration(700)
                                .repeat(1)
                                .playOn(txtPasswordLayoutAgain)
                        }

                    } else {
                        edtPasswordAgain.error = getString(R.string.pleaseEnterPassword)
                        edtPasswordAgain.requestFocus()
                        YoYo.with(Techniques.Shake)
                            .duration(700)
                            .repeat(1)
                            .playOn(txtPasswordLayoutAgain)
                    }

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
        mDbProperties.rememberMe = ""
        mDbProperties.username = ""
        mDbProperties.name = ""
        mDbProperties.surname = ""
        mDbProperties.photoPath = ""

        Log.d("Realm ","Set Item")

        presenter.setDbProperties("OK",mDbProperties)


    }

    override fun onSetRealmSuccess() {
        Log.d("Realm ","Success")
        navigateToLogin()
    }

    override fun navigateToLogin() {
        finish()
        val myIntent = Intent(this, LoginActivity::class.java)
            myIntent.putExtra("username", edtUserName.text.toString())
            myIntent.putExtra("photoPath", photoPath)
            startActivity(myIntent)
    }

    override fun showProgress() {
        registerLayout.isEnabled = false
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        registerLayout.isEnabled = true
        progress.visibility = View.GONE
    }

    override fun setError(error: String) {

        when(error) {
            "ConnectionError" -> ToastNotice(
                this,
                getString(R.string.connectionError),
                ToastNotice.ToastType.WARNING
            )
            "UsernameAlreadyTaken" -> ToastNotice(
                this,
                getString(R.string.usernameAlreadyTaken),
                ToastNotice.ToastType.WARNING
            )
            else -> ToastNotice(this, error, ToastNotice.ToastType.WARNING)
        }
    }

    private fun validatePermissions() : Boolean {

        val permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val permissionCAMERA = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)

        val listPermissionsNeeded = ArrayList<String>()
        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (permissionCAMERA != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.CAMERA)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 41)
            return false
        }
        return true
    }

    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {
        if (resultCode == Activity.RESULT_OK
            && requestCode == TAKE_PHOTO_REQUEST) {
            processCapturedPhoto()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun processCapturedPhoto() {
        val cursor = contentResolver.query(
            Uri.parse(mCurrentPhotoPath),
            Array(1) {android.provider.MediaStore.Images.ImageColumns.DATA},
            null, null, null)
        cursor?.moveToFirst()
        photoPath = cursor?.getString(0)
        cursor?.close()
        val file = File(photoPath)
        val uri = Uri.fromFile(file)

        imgProfilePhoto.setImageURI(uri)

    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}