package com.gentleman.github.network

import com.gentleman.common.ext.ensureDir
import com.gentleman.github.AppContext
import com.gentleman.github.network.interceptors.AcceptInterceptor
import com.gentleman.github.network.interceptors.AuthInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory2
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

private const val BASE_URL = "https://api.github.com"

private val cacheFile by lazy {
    File(AppContext.cacheDir, "webServiceApi").apply {
        ensureDir()
    }
}

val retrofit by lazy {
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJavaCallAdapterFactory2.createWithSchedulers(Schedulers.io(),AndroidSchedulers.mainThread()))
        .client(
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
//                .cache(Cache(cacheFile, 1024 * 1024 * 1024))
                .addInterceptor(AuthInterceptor())
                .addInterceptor(AcceptInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .baseUrl(BASE_URL)
        .build()
}