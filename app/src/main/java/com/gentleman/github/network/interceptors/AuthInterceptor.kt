package com.gentleman.github.network.interceptors

import android.util.Base64
import com.gentleman.github.model.account.AccountManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request();
        return chain.proceed(original.newBuilder()
            .apply {
                when {
                    //如果是鉴权接口
                    original.url().pathSegments().contains("authorizations") -> {
                        val userCredentials = AccountManager.username + ":" + AccountManager.password
                        val auth = "Basic " + String(Base64.encode(userCredentials.toByteArray(), Base64.DEFAULT)).trim()
                        header("Authorization", auth)
                    }
                    //已经登录的情况
                    AccountManager.isLoggedIn()->{
                        val auth = "Token "+ AccountManager.token
                        header("Authorization",auth)
                    }
                   else->removeHeader("Authorization")
                }
            }
            .build())
    }
}