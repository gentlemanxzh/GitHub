package com.gentleman.github.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response


class AcceptInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        return chain.proceed(original.newBuilder().apply {
            //这里记得把之前的accept加进去，没有的话就为空
            header("accept", "application/vnd.github.v3.full+json,${original.header("accept") ?: ""}")
        }.build())
    }
}