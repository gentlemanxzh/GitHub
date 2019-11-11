package com.gentleman.github

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import cn.carbs.android.avatarimageview.library.AvatarImageView
import com.bennyhuo.tieguanyin.annotations.Builder
import com.gentleman.common.ext.no
import com.gentleman.common.ext.otherwise
import com.gentleman.github.model.account.AccountManager
import com.gentleman.github.model.account.OnAccountStateChangeListener
import com.gentleman.github.network.entities.User
import com.gentleman.github.utils.doOnLayoutAvailable
import com.gentleman.github.utils.loadWithGlide
import com.gentleman.github.utils.showFragment
import com.gentleman.github.view.fragments.AboutFragment
import com.gentleman.github.view.startLoginActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.sdk15.listeners.onClick
import org.jetbrains.anko.toast

@Builder(flags = [Intent.FLAG_ACTIVITY_CLEAR_TOP])
class MainActivity : AppCompatActivity(),OnAccountStateChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawer_layout.setDrawerListener(toggle)
        toggle.syncState()
        initNavigationView()

        AccountManager.onAccountStateChangeListeners.add(this)

        showFragment(R.id.fragmentContainer,AboutFragment::class.java)
        title = "About"
    }

    override fun onDestroy() {
        super.onDestroy()
        AccountManager.onAccountStateChangeListeners.remove(this)
    }

    private fun initNavigationView() {
        AccountManager.currentUser?.let(::updateNavigationView) ?: clearNavigationView()
        initNavigationHeaderEvent()
    }

    private fun initNavigationHeaderEvent(){
        navigationView.doOnLayoutAvailable {
            navigationHeader.onClick {
                AccountManager.isLoggedIn().no {
                    startLoginActivity()
                }.otherwise {
                    AccountManager.logout()
                        .subscribe({
                            toast("注销成功")
                        },{
                            it.printStackTrace()
                        })
                }
            }
        }
    }

    private fun updateNavigationView(user: User) {
        navigationView.doOnLayoutAvailable {
            usernameView.text = user.login
            emailView.text = user.email ?: ""
            avatarView.loadWithGlide(user.avatar_url, user.login.first())
        }
    }

    private fun clearNavigationView() {
        navigationView.doOnLayoutAvailable {
            usernameView.text = "请登录"
            emailView.text = ""
            avatarView.imageResource = R.drawable.ic_github

        }
    }


    override fun onLogin(user: User) {
      updateNavigationView(user)
    }

    override fun onLogout() {
       clearNavigationView()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)){
            drawer_layout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressed()
        }
    }
}
