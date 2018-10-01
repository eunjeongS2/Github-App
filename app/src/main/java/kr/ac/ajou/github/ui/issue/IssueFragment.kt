package kr.ac.ajou.github.ui.issue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_issue.view.*
import kr.ac.ajou.github.R
import kr.ac.ajou.github.util.ViewPagerAdapter

class IssueFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_issue, container, false)

        val pageAdapter = fragmentManager?.let { ViewPagerAdapter(it) }
        val openClosedPageAdapter = fragmentManager?.let { ViewPagerAdapter(it) }

        pageAdapter?.addFragment(title = "Created", fragment = IssueCreatedFragment())
        pageAdapter?.addFragment(title = "Assigned", fragment = IssueAssignedFragment())
        pageAdapter?.addFragment(title = "Mentioned", fragment = IssueMentionedFragment())


        view.issueViewPager.adapter = pageAdapter
        view.issueTabLayout.setupWithViewPager(view.issueViewPager)

        for(i in 0 until (view.issueViewPager.adapter as ViewPagerAdapter).count){
            view.issueTabLayout.getTabAt(i)?.text = pageAdapter?.getFragmentInfo(i)?.text
        }

//        val openCount = "0"
//        val closedCount = "3"
//
//        openClosedPageAdapter?.addFragment(title = "$openCount Open", fragment = IssueCreatedOpenFragment())
//        openClosedPageAdapter?.addFragment(title = "$closedCount Closed", fragment = IssueCreatedClosedFragment())
//
//        view.issueOpenClosedViewPager.adapter = pageAdapter
//        view.issueOpenClosedTabLayout.setupWithViewPager(view.issueOpenClosedViewPager)
//
//        for(i in 0 until (view.issueOpenClosedViewPager.adapter as ViewPagerAdapter).count){
//            view.issueOpenClosedTabLayout.getTabAt(i)?.text = pageAdapter?.getFragmentInfo(i)?.text
//        }

        return view
    }
}