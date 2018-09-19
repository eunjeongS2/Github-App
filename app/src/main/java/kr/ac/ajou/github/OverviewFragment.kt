package kr.ac.ajou.github

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_overview.*
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kr.ac.ajou.github.api.getUser


class OverviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        view.post {
            val constraintSet = ConstraintSet()
            constraintSet.clone(overviewLayout)
            constraintSet.setVerticalBias(R.id.guideline,
                    ((((104.0/669.0)*view.contributionsWebView.width) + view.contributionTextView.height + 90)/view.height).toFloat())
            constraintSet.applyTo(overviewLayout)

            setContributionWebView(view.contributionsWebView)


            println(((((104.0/669.0)*view.contributionsWebView.width) + view.contributionTextView.height + 8)/view.height).toFloat())

        }
        return view
    }

    @SuppressLint("SetJavaScriptEnabled", "ClickableViewAccessibility")
    private fun setContributionWebView(webView: WebView){

        webView.visibility = View.INVISIBLE
        webView.loadUrl("https://github.com/users/${context?.let { getUser(it)?.login }}/contributions")

        webView.settings.javaScriptEnabled = true
        webView.setInitialScale(((webView.width.toDouble()/669)*100).toInt())
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true

        webView.setOnTouchListener { _, p1 ->
            return@setOnTouchListener (p1.action == MotionEvent.ACTION_MOVE)
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {

                view?.loadUrl("javascript:(function() { " +
                        "var head = document.getElementsByClassName('f4 text-normal mb-2')[0].style.display='none'; " +
                        "var down= document.getElementsByClassName('contrib-footer clearfix mt-1 mx-3 px-3 pb-1')[0].style.display='none'; " +
                        "})()")

                view?.visibility = View.VISIBLE
                view?.invalidate()
            }
        }
    }
}