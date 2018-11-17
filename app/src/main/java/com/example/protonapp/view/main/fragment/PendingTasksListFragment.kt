package com.example.protonapp.view.main.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.fragmnet_task_list.*

class PendingTasksListFragment : BaseTasksListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipes()
    }

    override fun getTasks() {
        viewModel.getPendingTasks()
    }

    private fun setupSwipes() {
        val leftSwipe = ItemTouchHelper(getSwipeHandler(ItemTouchHelper.LEFT, START_TASK_DELAY))
        leftSwipe.attachToRecyclerView(recyclerView)
        val rightSwipe = ItemTouchHelper(getSwipeHandler(ItemTouchHelper.RIGHT))
        rightSwipe.attachToRecyclerView(recyclerView)
    }

    companion object {
        const val START_TASK_DELAY = 60

        fun newInstance() = PendingTasksListFragment()
    }
}
