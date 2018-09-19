package kr.ac.ajou.github

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.util.Log
import kotlinx.android.synthetic.main.activity_auth.*
import kr.ac.ajou.github.api.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthActivity : AppCompatActivity() {

    companion object {
        // const val TAG = "SignInActivity"
        val TAG: String = AuthActivity::class.java.simpleName

        const val CLIENT_ID = "6a3b810e3ba7a410c16c"
        const val CLIENT_SECRET = "e9244140f7aad0181ed7f4c46366a77de3292923"
    }

    // apply, let: 수신 객체 지정 람다
//    private val loggingInterceptor = HttpLoggingInterceptor().apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
//
//    private val httpClient = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        oAuthButton.setOnClickListener {
            // https://github.com/login/oauth/authorize?client_id=XXX
            val authUri = Uri.Builder().scheme("https")
                    .authority("github.com")
                    .appendPath("login")
                    .appendPath("oauth")
                    .appendPath("authorize")
                    .appendQueryParameter("client_id", CLIENT_ID)
                    .build()

            // toast(authUri.toString())

            // 인터넷 브라우저를 실행하는 Intent
            // => Custom Tabs: android-support-library
            val intent = CustomTabsIntent.Builder().build()
            intent.launchUrl(this, authUri)
        }
    }

    // 기존의 액티비티가 다시 사용될 경우...
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        check(intent != null)
        check(intent?.data != null)

        val uri = intent?.data
        val code = uri?.getQueryParameter("code") ?: throw IllegalStateException("no code!!")

        getAccessToken(code)
    }


    private fun getAccessToken(code: String) {
        Log.i(TAG, "getAccessToken: $code")

        val call = authApi.getAccessToken(CLIENT_ID, CLIENT_SECRET, code)

        call.enqueue({
            it.body()?.let {
                getUser(it.accessToken)

                updateToken(this, it.accessToken)
            }
        }, {
            toast(it.message.toString())
        })

    }

    private fun getUser(token: String){
        val userApiCall = provideGithubApi(this).getUser(token)
        userApiCall.enqueue({
            it.body()?.let {

                updateUser(this, it)
                startActivity<MainActivity>()

            }
        }, {

        })
    }
}

fun <T> Call<T>.enqueue(success: (response: Response<T>) -> Unit, failure: (t: Throwable) -> Unit) {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>, t: Throwable) = failure(t)
        override fun onResponse(call: Call<T>, response: Response<T>) = success(response)
    })
}