package com.example.protonapp.view.main.fragment

import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.fragmnet_task_list.*

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
