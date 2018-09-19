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
    fun getEvent(@Path("user") path: String): Call<List<Event>>

    @GET("user")
    fun getUser(@Query("client_id") clientId: String): Call<User>

    @GET("users/{user}/repos")
    fun getPinnedRepositories(@Path("user") user: String): Call<List<Repository>>

}