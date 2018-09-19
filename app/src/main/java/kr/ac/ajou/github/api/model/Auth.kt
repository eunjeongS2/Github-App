package kr.ac.ajou.github.api.model

import com.google.gson.annotations.SerializedName

data class Auth(
        @field:SerializedName("access_token")
        val accessToken: String,
        @field:SerializedName("token_type")
        val tokenType: String)

data class User(val login: String, val name: String,
                @field:SerializedName("avatar_url")val image: String)

data class GithubRepo(@field:SerializedName("full_name") val fullName: String)

data class RepoSearchResponse(@field:SerializedName("total_count") val totalCount: Int,
                              val items: List<GithubRepo>)

data class Actor(val login: String, @field:SerializedName("avatar_url")val imageUrl: String)
data class Repo(val name: String)
data class Event(val type: String, val actor: Actor, val repo: Repo, @field:SerializedName("created_at")val time: String)
data class Repository(val name : String, val language: String, @field:SerializedName("stargazers_count")val stars: String,
                      @field:SerializedName("forks_count")val forks: String, @field:SerializedName("updated_at")val update: String)