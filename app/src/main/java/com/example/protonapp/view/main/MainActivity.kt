package com.example.protonapp.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.base.utils.extensions.start
import com.android.base.view.BaseActivity
import com.example.protonapp.R
import com.example.protonapp.utils.extension.addGradientBackground
import com.example.protonapp.view.main.fragment.FinishedTasksListFragment
import com.example.protonapp.view.main.fragment.PendingTasksListFragment
import com.example.protonapp.view.main.pageadapter.TaskListPageAdapter
import com.example.protonapp.view.newtask.CreateTaskActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.toolbar.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        toolbarTitle.text = getString(R.string.your_tasks)
        addGradientBackground()

        setupFab()
        setupViewPager()
    }

    private fun setupFab() {
        fab.setOnClickListener {
            start<CreateTaskActivity>()
        }
    }

    private fun setupViewPager() {
        val fragments = listOf<Fragment>(
                PendingTasksListFragment.newInstance(),
                FinishedTasksListFragment.newInstance())
        val tabTitles = resources.getStringArray(R.array.tab_titles)
        viewPager.adapter = TaskListPageAdapter(fragments, tabTitles, supportFragmentManager)
        tabLayout.setupWithViewPager(viewPager)
    }
}
