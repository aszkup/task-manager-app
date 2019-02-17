package com.example.taskmanagerapp.view.main.fragment

import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.fragmnet_task_list.*

/**
 * Finished Task List Fragment
 */
class FinishedTasksListFragment : BaseTasksListFragment() {

    override fun getTasks() {
        viewModel.getFinishedTasks()
    }

    override fun setupSwipes() {
        val leftSwipe = ItemTouchHelper(getSwipeHandler(ItemTouchHelper.LEFT))
        leftSwipe.attachToRecyclerView(recyclerView)
        val rightSwipe = ItemTouchHelper(getSwipeHandler(ItemTouchHelper.RIGHT))
        rightSwipe.attachToRecyclerView(recyclerView)
    }

    companion object {
        fun newInstance() = FinishedTasksListFragment()
    }
}
