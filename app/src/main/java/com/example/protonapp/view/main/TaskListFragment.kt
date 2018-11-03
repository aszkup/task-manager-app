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
import com.example.protonapp.viewmodel.main.TasksViewModel
import kotlinx.android.synthetic.main.fragmnet_task_list.*

class TaskListFragment : BaseFragment() {

    private lateinit var viewModel: TasksViewModel
    private lateinit var tasksAdapter: TaskListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getModel(TasksViewModel::class.java)
        viewModel.viewState.observe(this, Observer {
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragmnet_task_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTaskList()
    }

    private fun setupTaskList() {
        tasksAdapter = TaskListAdapter(mutableListOf())
        recyclerView.adapter = tasksAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    companion object {
        fun newInstance() = TaskListFragment()
    }
}
