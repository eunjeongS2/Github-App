package kr.ac.ajou.github

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_profile.view.*
import kr.ac.ajou.github.util.ViewPagerAdapter

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val pageAdapter = fragmentManager?.let { ViewPagerAdapter(it) }

        pageAdapter?.addFragment(title = "Overview", fragment = OverviewFragment())
        pageAdapter?.addFragment(title = "Repositories", fragment = RepositoriesFragment())
        pageAdapter?.addFragment(title = "Stars", fragment = StarsFragment())
        pageAdapter?.addFragment(title = "Followers", fragment = FollowersFragment())
        pageAdapter?.addFragment(title = "Following", fragment = FollowingFragment())

        view.profileViewPager.adapter = pageAdapter
        view.profileTabLayout.setupWithViewPager(view.profileViewPager)

        for(i in 0 until (view.profileViewPager.adapter as ViewPagerAdapter).count){
            view.profileTabLayout.getTabAt(i)?.text = pageAdapter?.getFragmentInfo(i)?.text
        }

        return view
    }
}