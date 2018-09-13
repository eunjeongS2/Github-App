package kr.ac.ajou.github.api

import android.content.Context
import android.preference.PreferenceManager

// App의 설정에 Access Token을 저장합니다.
// App 설정: PreferenceManager

const val KEY_AUTH_TOKEN = "kr.ac.ajou.github.auth_token"

fun updateToken(context: Context, token: String) {
    PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(KEY_AUTH_TOKEN, token)
            .apply()
}

fun getToken(context: Context) : String? {
    return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(KEY_AUTH_TOKEN, null)
}