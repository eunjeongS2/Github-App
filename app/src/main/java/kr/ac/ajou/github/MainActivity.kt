package kr.ac.ajou.github

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBarDrawerToggle
import android.view.LayoutInflater
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*
import kr.ac.ajou.github.api.getUser
import kr.ac.ajou.github.util.ViewPagerAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val pageAdapter = ViewPagerAdapter(supportFragmentManager)

        pageAdapter.addFragment(R.drawable.ic_home, "Home", HomeFragment())
        pageAdapter.addFragment(R.drawable.ic_issue_opened, "Issues", IssueFragment())
        pageAdapter.addFragment(R.drawable.ic_pull_request, "Pull Request", IssueFragment())
        pageAdapter.addFragment(R.drawable.ic_person, "Profile", ProfileFragment())

        viewPager.adapter = pageAdapter
        tabLayout.setupWithViewPager(viewPager)

        for(i in 0 until (viewPager.adapter as ViewPagerAdapter).count){
//            tabLayout.getTabAt(i)?.setCustomView(R.layout.tab_layout)
            tabLayout.getTabAt(i)?.setIcon(pageAdapter.getFragmentInfo(i).iconResId)

        }
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        searchView.setOnQueryTextFocusChangeListener { _, b ->
            if(b)
                logoImageView.visibility = View.INVISIBLE
            else
                logoImageView.visibility = View.VISIBLE
        }

        setNavigationView()

    }

    private fun setNavigationView(){
        val header = LayoutInflater.from(this).inflate(R.layout.drawer_header, navigationView)

        with(header) {
            loginTextView.text = getUser(this@MainActivity)?.login
            nameTextView.text = getUser(this@MainActivity)?.name

            kr.ac.ajou.github.GlideApp.with(this)
                    .load(getUser(this@MainActivity)?.image)
                    .into(userImageView)

        }
    }
}
