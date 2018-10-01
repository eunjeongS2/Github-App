package kr.ac.ajou.github.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_followers.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import kr.ac.ajou.github.R
import kr.ac.ajou.github.api.getUser
import kr.ac.ajou.github.api.model.User
import kr.ac.ajou.github.api.provideGithubApi
import kr.ac.ajou.github.ui.GlideApp
import kr.ac.ajou.github.ui.enqueue
import kotlin.properties.Delegates

class FollowersFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_followers, container, false)

        val followersCall = context?.let { context ->
            getUser(context)?.login?.let { user ->
                provideGithubApi(context).getUserFollowers(user)
            }
        }

        followersCall?.enqueue({
            it.body()?.let {

                setUserRecyclerView(view.followersRecyclerView, it)

            }
        }, {

        })


        return view
    }


}

fun setUserRecyclerView(recyclerView: RecyclerView, users: List<User>) {
    val userAdapter = UserAdapter()
    userAdapter.items = users

    recyclerView.run {
        layoutManager = LinearLayoutManager(context)
        adapter = userAdapter
    }
}

class UserViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))

class UserAdapter : RecyclerView.Adapter<UserViewHolder>() {
    var items: List<User> by Delegates.observable(emptyList()) { _, _, _ ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(parent)
    override fun getItemCount() = items.count()
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = items[position]

        with(holder.itemView) {

            userLoginTextView.text = item.login

            GlideApp.with(this)
                    .load(item.image)
                    .into(usersImageView)

            val userCall = provideGithubApi(context).getUserName(item.login)
            userCall.enqueue({
                it.body()?.let {
                    userNameTextView.text = it.name
                }
            }, {

            })


        }
    }
}