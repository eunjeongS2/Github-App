package kr.ac.ajou.github.ui.issue

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_issue_created.view.*
import kr.ac.ajou.github.R
import kr.ac.ajou.github.util.ViewPagerAdapter

class IssueCreatedFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_issue_created, container, false)

        val pageAdapter = fragmentManager?.let { ViewPagerAdapter(it) }

        val openCount = "0"
        val closedCount = "3"

        pageAdapter?.addFragment(title = "$openCount Open", fragment = IssueCreatedOpenFragment())
        pageAdapter?.addFragment(title = "$closedCount Open", fragment = IssueCreatedClosedFragment())

        view.issueCreatedViewPager.adapter = pageAdapter
        view.issueCreatedTabLayout.setupWithViewPager(view.issueCreatedViewPager)

        for(i in 0 until (view.issueCreatedViewPager.adapter as ViewPagerAdapter).count){
            view.issueCreatedTabLayout.getTabAt(i)?.text = pageAdapter?.getFragmentInfo(i)?.text
        }

        return view
    }
}