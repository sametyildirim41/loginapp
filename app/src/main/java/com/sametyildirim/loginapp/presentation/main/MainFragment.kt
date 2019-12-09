package com.sametyildirim.loginapp.presentation.main


import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sametyildirim.loginapp.R
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.helper.ToastNotice
import com.sametyildirim.loginapp.presentation.base.BaseActivity
import com.sametyildirim.loginapp.presentation.base.BaseFragment
import com.sametyildirim.loginapp.presentation.login.LoginActivity
import kotlinx.android.synthetic.main.content_main.*


class MainFragment : BaseFragment(), MainFragmentVP.View {

    private lateinit var presenter : MainFragmentVP.Presenter
    private lateinit var currentUser: User

    override fun getLayout(): Int {
        return R.layout.content_main
    }

    override fun setFragment(baseFragment: BaseFragment, bundle: Bundle?) {
        baseFragment.attachPresenter { presenter }
        baseFragment.arguments = bundle
        activity!!.supportFragmentManager.beginTransaction().replace(R.id.fragment_container,baseFragment).commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter = MainFragmentPresenter(this)
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.app_name)

        setItems(BaseActivity().getUserInfo())

        BaseActivity().getUserInfo()

    }

    private fun setItems(item: User) {
        currentUser = item
        txtUsername.text = item.username
        txtName.text = item.name
        txtSurname.text = item.surname
        if(!item.photoPath.isNullOrEmpty())
            Glide.with(this).load(item.photoPath).into(imgProfilePhoto)
    }

    override fun executeDeleteUser() {
        presenter.deleteUser(currentUser)
    }

    override fun navigateToLogin() {
        activity!!.finish()
        val myIntent = Intent(activity!!, LoginActivity::class.java)
        startActivity(myIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater!!.inflate(R.menu.main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId) {
            R.id.actionDeleteProfile -> {
                presenter.dialogDeleteUser(activity!!,"dialogDeleteUser",getString(R.string.deleteUser).toUpperCase(),getString(R.string.doYouWantToDeleteThisUser,currentUser.username.toString()),"",getString(R.string.yes),getString(R.string.no))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

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

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.onDestroy()
    }

}
