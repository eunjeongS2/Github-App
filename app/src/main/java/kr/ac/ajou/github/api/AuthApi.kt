package kr.ac.ajou.github.api

import retrofit2.Call
import retrofit2.http.*
import kr.ac.ajou.github.api.model.Auth

// https://github.com
interface AuthApi {

    @FormUrlEncoded
    @POST("login/oauth/access_token")
    @Headers("Accept: application/json")
    fun getAccessToken(@Field("client_id") clientId: String,
                       @Field("client_secret") clientSecret: String,
                       @Field("code") code: String): Call<Auth>

}