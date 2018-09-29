package kr.ac.ajou.github

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_issue.view.*
import kr.ac.ajou.github.util.ViewPagerAdapter

class IssueFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_issue, container, false)

        val pageAdapter = fragmentManager?.let { ViewPagerAdapter(it) }

        pageAdapter?.addFragment(title = "Created", fragment = CreatedFragment())
        pageAdapter?.addFragment(title = "Assigned", fragment = AssignedFragment())
        pageAdapter?.addFragment(title = "Mentioned", fragment = MentionedFragment())

        view.issueViewPager.adapter = pageAdapter
        view.issueTabLayout.setupWithViewPager(view.issueViewPager)

        for(i in 0 until (view.issueViewPager.adapter as ViewPagerAdapter).count){
            view.issueTabLayout.getTabAt(i)?.text = pageAdapter?.getFragmentInfo(i)?.text
        }

        return view
    }
}