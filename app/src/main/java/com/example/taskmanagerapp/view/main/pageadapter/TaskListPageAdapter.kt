package com.example.taskmanagerapp.view.main.pageadapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

/**
 * Task [FragmentStatePagerAdapter]
 */
class TaskListPageAdapter(
        private val fragments: List<Fragment>,
        private val tabTitles: Array<String>,
        fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(fragmentManager) {


    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = tabTitles[position]
}
