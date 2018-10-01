package kr.ac.ajou.github.ui.profile

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_repositories.view.*
import kotlinx.android.synthetic.main.item_repository.view.*
import kr.ac.ajou.github.R
import kr.ac.ajou.github.api.getUser
import kr.ac.ajou.github.api.model.Repository
import kr.ac.ajou.github.api.provideGithubApi
import kr.ac.ajou.github.ui.enqueue
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import kotlin.properties.Delegates

class RepositoriesFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_repositories, container, false)

        val repositoryCall = context?.let { context ->
            getUser(context)?.login?.let{ user ->
                provideGithubApi(context).getUserRepositories(user) } }

        repositoryCall?.enqueue({
            it.body()?.let {
                setRepositoryRecyclerView(view.repositoriesRecyclerView, it, javaClass.simpleName)
            }
        }, {

        })

        return view
    }

}

fun setRepositoryRecyclerView(recyclerView: RecyclerView, repositories: List<Repository>, fragment: String){
    val repositoriesAdapter = RepositoriesAdapter(fragment)
    repositoriesAdapter.items = repositories

    recyclerView.run {
        layoutManager = LinearLayoutManager(context)
        adapter = repositoriesAdapter
        addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
    }
}

class RepositoriesViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false))

class RepositoriesAdapter(val fragment: String) : RecyclerView.Adapter<RepositoriesViewHolder>() {
    var items: List<Repository> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = RepositoriesViewHolder(parent)
    override fun getItemCount() = items.count()
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RepositoriesViewHolder, position: Int) {
        val item = items[position]
        val languageColor = generateLanguage(item.language)

        with(holder.itemView) {
            repositoryName.text = if (fragment == "StarsFragment") item.fullName else item.name

            val gradientDrawable = languageImageView.drawable as GradientDrawable
            gradientDrawable.setColor(ContextCompat.getColor(context, languageColor))

            languageTextView.text = item.language

            starsTextView.text = item.stars
            forkedTextView.text = item.forks

            val formatter = DateTimeFormat.forPattern("MMM dd, yyyy")

            val time = LocalDateTime.parse(item.update.substring(0, item.update.length-1))
            dateTextView.text = time.toString(formatter)


        }
    }
}