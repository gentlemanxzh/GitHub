package com.gentleman.github.network.services

import com.gentleman.github.network.entities.Repository
import com.gentleman.github.network.entities.SearchRepositories
import com.gentleman.github.network.retrofit
import okhttp3.Response
import retrofit2.adapter.rxjava.GitHubPaging
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

interface RepositoryApi {

    @GET("/users/{owner}/repos?type=all")
    fun listRepositoriesOfUser(@Path("owner")owner:String,@Query("page")page:Int,@Query("per_page")per_page:Int=20):Observable<GitHubPaging<Repository>>

    @GET("/search/repositories?order=desc&sort=updated")
    fun allRepositories(@Query("page") page: Int = 1, @Query("q") q: String, @Query("per_page") per_page: Int = 20): Observable<SearchRepositories>

    object RepositoryService : RepositoryApi by retrofit.create(RepositoryApi::class.java)
}