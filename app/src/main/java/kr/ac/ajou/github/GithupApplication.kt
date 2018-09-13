package kr.ac.ajou.github

import android.app.Application
import net.danlew.android.joda.JodaTimeAndroid

class GithubApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        JodaTimeAndroid.init(this)
    }
}