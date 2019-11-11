package com.gentleman.github.presenter

import com.gentleman.github.BuildConfig
import com.gentleman.github.model.account.AccountManager
import com.gentleman.github.view.LoginActivity
import com.gentleman.mvp.impl.BasePresenter

class LoginPresenter: BasePresenter<LoginActivity>() {

    fun doLogin(name:String,password:String){
        AccountManager.username = name
        AccountManager.password = password
        view.onLoginStart()
        AccountManager.login().subscribe({
            view.onLoginSuccess()
        },{
            view.onLoginError(it)
        })
    }

    fun checkUserName(name:String):Boolean{
        return true
    }

    fun checkPassword(passwd: String):Boolean{
        return true
    }

    override fun onResume() {
        super.onResume()
        if(BuildConfig.DEBUG){
            view.onDataInit(BuildConfig.testUserName,BuildConfig.testPassword)
        }else{
            view.onDataInit(AccountManager.username,AccountManager.password)
        }
    }
}