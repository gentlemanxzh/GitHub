package com.gentleman.github.model.account

import com.gentleman.github.network.entities.AuthorizationReq
import com.gentleman.github.network.entities.AuthorizationRsp
import com.gentleman.github.network.entities.User
import com.gentleman.github.network.services.AuthService
import com.gentleman.github.network.services.UserService
import com.gentleman.github.utils.fromJson
import com.gentleman.github.utils.pref
import com.google.gson.Gson
import retrofit2.HttpException
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

object AccountManager {
    var username by pref("")
    var password by pref("")
    var token by pref("")
    var authId by pref(0)
    private var userJson by pref("")

    val onAccountStateChangeListeners = ArrayList<OnAccountStateChangeListener>()

    private fun notifyLogin(user: User) {
        onAccountStateChangeListeners.forEach {
            it.onLogin(user)
        }
    }

    private fun notifyLogout() {
        onAccountStateChangeListeners.forEach {
            it.onLogout()
        }
    }


    var currentUser: User? = null
        get() {
            if (field == null && userJson.isNotEmpty()) {
                field = Gson().fromJson<User>(userJson)
            }
            return field
        }
        set(value) {
            if (value == null) {
                userJson = ""
            } else {
                userJson = Gson().toJson(field)
            }
            field = value
        }

    fun isLoggedIn(): Boolean = token.isNotEmpty()


    fun login() =
        AuthService.createAuthorization(AuthorizationReq())
            .doOnNext {
                if (it.token.isEmpty()) throw AccountException(it)
            }
            .retryWhen {
                it.flatMap {
                    //如果是因为已经登录之后再登录而产生的错误，就先把token删除
                    if (it is AccountException) {
                        AuthService.deleteAuthorization(it.authorizationRsp.id)
                    } else {
                        Observable.error(it)
                    }
                }
            }
            .flatMap {
                token = it.token
                authId = it.id
                UserService.getAuthenticatedUser()
            }.map {
                currentUser = it
                notifyLogin(it)
            }

    fun logout() = AuthService.deleteAuthorization(authId)
        .doOnNext {
            if (it.isSuccessful) {
                authId = -1
                token = ""
                currentUser = null
                notifyLogout()
            } else {
                throw HttpException(it)
            }
        }

    class AccountException(val authorizationRsp: AuthorizationRsp) : Exception("Already logged in.")

}

interface OnAccountStateChangeListener {
    fun onLogin(user: User)

    fun onLogout()
}