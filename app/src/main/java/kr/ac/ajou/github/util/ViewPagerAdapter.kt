package kr.ac.ajou.github.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

data class FragmentInfo(val iconResId: Int, val text: String, val fragment: Fragment)

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragmentInfoList = ArrayList<FragmentInfo>()

    fun addFragment(iconResId: Int = 0, title: String, fragment: Fragment) {
        val info = FragmentInfo(iconResId, title, fragment)
        fragmentInfoList.add(info)
    }

    fun getFragmentInfo(position: Int): FragmentInfo = fragmentInfoList[position]

    override fun getItem(position: Int): Fragment = fragmentInfoList[position].fragment

    override fun getPageTitle(position: Int): CharSequence? = null

    override fun getCount(): Int = fragmentInfoList.size

}