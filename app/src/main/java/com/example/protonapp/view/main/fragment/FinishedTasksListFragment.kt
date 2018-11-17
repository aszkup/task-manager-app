package com.example.protonapp.view.main.fragment

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import kotlinx.android.synthetic.main.fragmnet_task_list.*

class FinishedTasksListFragment : BaseTasksListFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipes()
    }

    override fun getTasks() {
        viewModel.getFinishedTasks()
    }

    private fun setupSwipes() {
        val leftSwipe = ItemTouchHelper(getSwipeHandler(ItemTouchHelper.LEFT))
        leftSwipe.attachToRecyclerView(recyclerView)
    }

    companion object {
        fun newInstance() = FinishedTasksListFragment()
    }
}
