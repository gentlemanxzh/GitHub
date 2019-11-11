package com.gentleman.github.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bennyhuo.tieguanyin.annotations.Builder
import com.gentleman.common.ext.otherwise
import com.gentleman.common.ext.yes
import com.gentleman.github.R
import com.gentleman.github.presenter.LoginPresenter
import com.gentleman.github.startMainActivity
import com.gentleman.github.utils.hideSoftInput
import com.gentleman.mvp.impl.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.sdk15.listeners.onClick
import org.jetbrains.anko.toast
@Builder(flags = [Intent.FLAG_ACTIVITY_NO_HISTORY])
class LoginActivity : BaseActivity<LoginPresenter>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        signInButton.onClick {
            presenter.checkUserName(username.text.toString()).yes {
                presenter.checkPassword(password.text.toString()).yes {
                    hideSoftInput()
                    presenter.doLogin(username.text.toString(), password.text.toString())
                }.otherwise {
                    showTips(password, "密码不合法")
                }
            }.otherwise {
                showTips(username, "用户名不合法")
            }
        }
    }

    private fun showProgress(show: Boolean) {
        val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        loginForm.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 0 else 1).toFloat()
        ).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginForm.visibility = if (show) View.GONE else View.VISIBLE
            }
        })

        loginProgress.animate().setDuration(shortAnimTime.toLong()).alpha(
            (if (show) 1 else 0).toFloat()
        ).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                loginProgress.visibility = if (show) View.VISIBLE else View.GONE
            }
        })
    }

    private fun showTips(view: EditText, tips: String) {
        view.requestFocus()
        view.error = tips
    }

    fun onLoginStart() {
        showProgress(true)
    }

    fun onLoginError(t: Throwable) {
        t.printStackTrace()
        toast("登录失败")
        showProgress(false)
    }

    fun onLoginSuccess() {
        toast("登录成功")
        startMainActivity()
        showProgress(false)
    }

    fun onDataInit(name: String, passwd: String) {
        username.setText(name)
        password.setText(passwd)
    }
}