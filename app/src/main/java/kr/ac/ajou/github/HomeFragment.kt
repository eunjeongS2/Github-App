package kr.ac.ajou.github

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.item_home.view.*
import kr.ac.ajou.github.api.getUser
import kr.ac.ajou.github.api.model.Event
import kr.ac.ajou.github.api.provideGithubApi
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import kotlin.properties.Delegates

class HomeFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val eventApiCall = context?.let { context -> getUser(context)?.let { user -> provideGithubApi(context).getEvent(user.login) } }
        eventApiCall?.enqueue({
            it.body()?.let {
                Log.i("HomeFragment", it.toString())

                val adapter = EventAdapter()
                view.homeRecyclerView.layoutManager = LinearLayoutManager(context)
                adapter.items = it
                view.homeRecyclerView.adapter = adapter
                view.homeRecyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager(context).orientation))
            }
        }, {

        })

        return view
    }
}

class EventViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false))

class EventAdapter : RecyclerView.Adapter<EventViewHolder>() {
    var items: List<Event> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = EventViewHolder(parent)
    override fun getItemCount() = items.count()
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val item = items[position]
        val type = generateType(items[position].type)

        with(holder.itemView) {
            homeIdTextView.text = item.actor.login
            homeIconImageView.setImageResource(type.second)
            homeBodyTextView.text = type.first+" "+item.repo.name

            GlideApp.with(this)
                    .load(item.actor.imageUrl)
                    .into(homeProfileImageView)

            val formatter = DateTimeFormat.forPattern("MMM dd, yyyy")

            val time = LocalDateTime.parse(item.time.substring(0, item.time.length-1))
            homeDateTextView.text = time.toString(formatter)

        }
    }

    private fun generateType(type: String): Pair<String, Int>{
        return when(type) {
            "CreateEvent" -> Pair("created a repository at", R.drawable.ic_repo)
            "ForkEvent" -> Pair("forked from", R.drawable.ic_repo_forked)
            "WatchEvent" -> Pair("starred", R.drawable.ic_star)
            else -> Pair("",R.drawable.ic_repo_push)
        }
    }
}


@GlideModule
class AppGlideModule : AppGlideModule()