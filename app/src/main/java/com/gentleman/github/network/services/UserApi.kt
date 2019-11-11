package com.gentleman.github.network.services

import com.gentleman.github.network.entities.User
import com.gentleman.github.network.retrofit
import retrofit2.http.GET
import rx.Observable

interface UserApi {
    @GET("/user")
    fun getAuthenticatedUser():Observable<User>
}

object UserService:UserApi by retrofit.create(UserApi::class.java)