package com.sametyildirim.loginapp.presentation.editprofile


import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import com.sametyildirim.loginapp.R
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.helper.ToastNotice
import com.sametyildirim.loginapp.presentation.base.BaseActivity
import com.sametyildirim.loginapp.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.content_edit_profile.*
import java.io.ByteArrayOutputStream
import java.io.File

class EditProfileFragment : BaseFragment(), EditProfileFragmentVP.View {

    private lateinit var presenter : EditProfileFragmentVP.Presenter
    private val TAKE_PHOTO_REQUEST = 101
    private var mCurrentPhotoPath: String = ""
    private var photoPath: String? = ""

    override fun getLayout(): Int {
        return R.layout.content_edit_profile
    }

    override fun setFragment(baseFragment: BaseFragment, bundle: Bundle?) {
        baseFragment.attachPresenter { presenter }
        baseFragment.arguments = bundle
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,baseFragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = EditProfileFragmentPresenter(this)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.editProfile)

        setCurrentUser(BaseActivity().getUserInfo())

        btnUpdate.setOnClickListener {

            validateCredentials()

        }

        imgProfilePhoto.setOnClickListener {
            if(validatePermissions()){
                launchCamera()
            }
        }

    }

    private fun setCurrentUser(user: User) {

        txtUserName.text = user.username!!
        if(!user.photoPath.isNullOrEmpty())
        {
            photoPath = user.photoPath
            Glide.with(this).load(photoPath).into(imgProfilePhoto)
        }
    }

    private fun validateCredentials(){


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
                            mUserReq.username = txtUserName.text.toString()
                            mUserReq.password = edtPassword.text.toString()
                            mUserReq.name = edtName.text.toString()
                            mUserReq.surname = edtSurName.text.toString()
                            presenter.updateUser(mUserReq, data)

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

            }

    override fun setItem(item: User) {
        BaseActivity().setUserInfo(item)
        presenter.navigateToMainFragment(null)
    }

    override fun showProgress() {
        editProfileLayout.isEnabled = false
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        editProfileLayout.isEnabled = true
        progress.visibility = View.GONE
    }

    override fun setError(error: String) {

        when(error) {
            "ConnectionError" -> ToastNotice(
                activity!!,
                getString(R.string.connectionError),
                ToastNotice.ToastType.WARNING
            )
            "UserNotFound" -> ToastNotice(
                activity!!,
                getString(R.string.usernameAlreadyTaken),
                ToastNotice.ToastType.WARNING
            )
            else -> ToastNotice(activity!!, error, ToastNotice.ToastType.WARNING)
        }
    }

    private fun validatePermissions() : Boolean {

        val permissionWRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(activity!!, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissionsNeeded = ArrayList<String>()
        if (permissionWRITE_EXTERNAL_STORAGE != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(activity!!, listPermissionsNeeded.toTypedArray(), 41)
            return false
        }
        return true
    }

    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = activity!!.contentResolver
            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(activity!!.packageManager) != null) {
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
        val cursor = activity!!.contentResolver.query(
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

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

}
