package kr.ac.ajou.github.api

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import com.google.gson.Gson
import kr.ac.ajou.github.api.model.User

const val KEY_AUTH_USER = "kr.ac.ajou.github.auth_user"

fun updateUser(context: Context, user: User) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_AUTH_USER, Gson().toJson(user))
            .apply()
}

fun getUser(context: Context) : User? {
    val user = android.preference.PreferenceManager.getDefaultSharedPreferences(context)
    .getString(KEY_AUTH_USER, null)
    Log.i("User", user)
    return Gson().fromJson(user, User::class.java)
}