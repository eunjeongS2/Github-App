package kr.ac.ajou.github.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import kr.ac.ajou.github.api.model.RepoSearchResponse
import kr.ac.ajou.github.api.model.Event
import kr.ac.ajou.github.api.model.Repository
import kr.ac.ajou.github.api.model.User
import retrofit2.http.Path

interface GithubApi {

    @GET("search/repositories")
    fun searchRepository(@Query("q") query: String): Call<RepoSearchResponse>

    @GET("users/{user}/received_events/public")
    fun getEvent(@Path("user") user: String): Call<List<Event>>

    @GET("user")
    fun getUser(@Query("client_id") clientId: String): Call<User>

    @GET("users/{user}/repos")
    fun getUserRepositories(@Path("user") user: String): Call<List<Repository>>

    @GET("users/{user}/starred")
    fun getUserStarredRepositories(@Path("user") user: String): Call<List<Repository>>

    @GET("/users/{user}/followers")
    fun getUserFollowers(@Path("user") user: String): Call<List<User>>

    @GET("/users/{user}")
    fun getUserName(@Path("user") user: String): Call<User>

    @GET("/users/{user}/following")
    fun getUserFollowing(@Path("user") user: String): Call<List<User>>

}