package com.example.protonapp.view.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.android.base.utils.extensions.start
import com.android.base.view.BaseActivity
import com.example.protonapp.R
import com.example.protonapp.view.newtask.CreateTaskActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        setupFab()
        setupViewPager()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupFab() {
        fab.setOnClickListener {
            start<CreateTaskActivity>()
        }
    }

    private fun setupViewPager() {
        val fragments = listOf<Fragment>(
                TaskListFragment.newInstance(),
                TaskListFragment.newInstance())
        val tabTitles = resources.getStringArray(R.array.tab_titles)
        viewPager.adapter = TaskListPageAdapter(fragments, tabTitles, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}
