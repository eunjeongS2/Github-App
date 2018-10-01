package kr.ac.ajou.github.ui.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_following.view.*
import kr.ac.ajou.github.R
import kr.ac.ajou.github.api.getUser
import kr.ac.ajou.github.api.provideGithubApi
import kr.ac.ajou.github.ui.enqueue

class FollowingFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_following, container, false)

        val followingCall = context?.let { context ->
            getUser(context)?.login?.let { user ->
                provideGithubApi(context).getUserFollowing(user)
            }
        }

        followingCall?.enqueue({
            it.body()?.let {

                setUserRecyclerView(view.followingRecyclerView, it)

            }
        }, {

        })

        return view
    }
}