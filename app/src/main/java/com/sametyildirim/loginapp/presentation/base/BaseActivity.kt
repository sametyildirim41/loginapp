package com.sametyildirim.loginapp.presentation.base

import android.content.Intent
import android.os.Bundle
import androidx.core.view.GravityCompat
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.sametyildirim.loginapp.R
import com.sametyildirim.loginapp.domain.model.response.User
import com.sametyildirim.loginapp.presentation.login.LoginActivity
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlin.collections.HashMap

class BaseActivity : AppCompatActivity(), BaseActivityVP.View, NavigationView.OnNavigationItemSelectedListener{

    private lateinit var presenter: BaseActivityPresenter

    companion object {
        private var previousFragmentList: MutableList<String> = mutableListOf()
        private var fragmentDatas = HashMap<String,Bundle?>()
        private var currentUser = User()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        currentUser.username = intent.getStringExtra("username")
        currentUser.photoPath = intent.getStringExtra("photoPath")
        currentUser.name = intent.getStringExtra("name")
        currentUser.surname = intent.getStringExtra("surname")

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, 0, 0)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)

        presenter = BaseActivityPresenter(this, BaseInteractor())
        presenter.getProfileInfo()

        presenter.navigateToMainFragment(null)

    }


    override fun setFragment(baseFragment: BaseFragment, bundle: Bundle?) {
        baseFragment.attachPresenter { presenter }
        baseFragment.arguments = bundle
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container,baseFragment).commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when(id) {
            R.id.nav_main -> {
                toolbar.title = getString(R.string.main)
                presenter.navigateToMainFragment(null)
            }
            R.id.nav_editProfile -> {
                toolbar.title = getString(R.string.editProfile)
                presenter.navigateToEditProfileFragment(null)
            }
            R.id.nav_logout -> {
                presenter.logout(this,"logout",getString(R.string.logout).toUpperCase(),getString(R.string.doYouWantToLogoutFromThisApplication),"",getString(R.string.yes),getString(R.string.no))
            }

        }

        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun navigateToLogin() {
        finish()
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun profileInfo(profileName: String, profileEmail: String, photoPath: String?) {
         txtProfileNameNav?.text = profileName
         txtProfileEmailNav?.text = profileEmail
        if(!photoPath.isNullOrEmpty()){
            Glide.with(this).load(photoPath).into(imgProfilePhotoNav)
        }
    }

    override fun onBackPressed() {

        when(previousFragmentList.findLast { true })
        {
            "MainFragment" -> {
                val bundle: Bundle? = fragmentDatas["MainFragment"]
                presenter.navigateToMainFragment(bundle)
                previousFragmentList.removeAt(previousFragmentList.lastIndex)
            }
            "EditProfileFragment" -> {
                val bundle: Bundle? = fragmentDatas["EditProfileFragment"]
                presenter.navigateToEditProfileFragment(bundle)
                previousFragmentList.removeAt(previousFragmentList.lastIndex)
            }

            else -> when(toolbar.title)
            {
                getString(R.string.app_name) -> presenter.logout(this,"logout",getString(R.string.logout).toUpperCase(),getString(R.string.doYouWantToLogoutFromThisApplication),"",getString(R.string.yes),getString(R.string.no))
                else -> presenter.navigateToMainFragment(null)
            }

        }
    }

    // Geçmiş fragmentları açılış sırasına göre sıralamak ve fragment açılışında kullandıkları verileri tutmak için oluşturduğum kurgu
    fun setPreviousFragment(fragmentName: String)
    {
        previousFragmentList.add(fragmentName)
    }

    fun setThisFragmentDatas(fragmentName: String, bundle: Bundle?)
    {
        fragmentDatas[fragmentName] = bundle
    }


    fun getUserInfo() : User{
        return currentUser
    }

    fun setUserInfo(mUser : User) {
        currentUser = mUser
    }


}