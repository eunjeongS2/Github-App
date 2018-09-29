package kr.ac.ajou.github

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.constraint.ConstraintSet
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_overview.view.*
import kotlinx.android.synthetic.main.item_pinned_repositories.view.*
import kr.ac.ajou.github.api.getUser
import kr.ac.ajou.github.api.model.Repository
import kr.ac.ajou.github.api.provideGithubApi
import kotlin.properties.Delegates


class OverviewFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        view.post {
            setContributionsHeight(view)
            setContributionWebView(view.contributionsWebView)

        }

        val repositoryCall = context?.let { context ->
            getUser(context)?.login?.let{ user ->
                provideGithubApi(context).getUserRepositories(user) } }

        repositoryCall?.enqueue({
            it.body()?.let {

                setRecyclerView(view.pinnedRepositoriesRecyclerView, it)

            }
        }, {

        })

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

    private fun setRecyclerView(recyclerView: RecyclerView, repositories: List<Repository>){
        val adapter = PinnedRepositoriesAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.items = repositories
        recyclerView.adapter = adapter
    }

    private fun setContributionsHeight(view: View){
        val constraintSet = ConstraintSet()
        constraintSet.clone(view.overviewLayout)
        constraintSet.setVerticalBias(R.id.guideline,
                ((((104.0/669.0)*view.contributionsWebView.width) + view.contributionTextView.height + 90)/view.height).toFloat())
        constraintSet.applyTo(view.overviewLayout)
    }
}


class PinnedRepositoriesViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_pinned_repositories, parent, false))

class PinnedRepositoriesAdapter : RecyclerView.Adapter<PinnedRepositoriesViewHolder>() {
    var items: List<Repository> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PinnedRepositoriesViewHolder(parent)
    override fun getItemCount() = items.count()
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PinnedRepositoriesViewHolder, position: Int) {
        val item = items[position]
        val languageColor = generateLanguage(item.language)

        with(holder.itemView) {
            repositoryName.text = item.name

            val gradientDrawable = languageImageView.drawable as GradientDrawable
            gradientDrawable.setColor(ContextCompat.getColor(context, languageColor))

            languageTextView.text = item.language

            starsTextView.text = item.stars
        }
    }
}

fun generateLanguage(language: String?): Int{
    return when(language) {
        "Java" -> R.color.colorJava
        "Kotlin" -> R.color.colorKotlin
        "JavaScript" -> R.color.colorJavaScript
        "HTML" -> R.color.colorHTML
        "Swift" -> R.color.colorSwift
        "PHP" -> R.color.colorPHP
        "Objective-C" -> R.color.colorObjectiveC
        null -> R.color.colorWhite

        else -> R.color.colorSwift
    }
}