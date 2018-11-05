package com.example.protonapp.view.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.view.BaseFragment
import com.example.protonapp.R
import com.example.protonapp.viewmodel.main.PendingTasksViewModel
import kotlinx.android.synthetic.main.fragmnet_task_list.*

class TaskListFragment : BaseFragment() {

    private lateinit var viewModelPending: PendingTasksViewModel
    private lateinit var tasksAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelPending = getModel(PendingTasksViewModel::class.java)
        viewModelPending.viewState.observe(this, Observer {
            tasksAdapter.submitList(it)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragmnet_task_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTaskList()
        viewModelPending.getTasks()
    }

    private fun setupTaskList() {
        tasksAdapter = TaskListAdapter()
        recyclerView.adapter = tasksAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}
