package com.example.taskmanagerapp.view.main.fragment

import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.fragmnet_task_list.*

/**
 * Pending Task List Fragment
 */
class PendingTasksListFragment : BaseTasksListFragment() {

    override fun getTasks() {
        viewModel.getPendingTasks()
    }

    override fun setupSwipes() {
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
