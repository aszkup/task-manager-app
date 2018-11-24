package com.example.protonapp.view.main.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.base.utils.enums.GENERAL_ERROR
import com.android.base.utils.enums.GENERAL_MESSAGE
import com.android.base.utils.extensions.componentFor
import com.android.base.utils.extensions.showToast
import com.android.base.utils.extensions.startActivity
import com.android.base.view.BaseFragment
import com.example.protonapp.R
import com.example.protonapp.repository.task.Task
import com.example.protonapp.utils.FileUtils
import com.example.protonapp.utils.WorkManagerUtils
import com.example.protonapp.view.main.TaskListAdapter
import com.example.protonapp.view.main.swipecallback.TaskSwipeCallback
import com.example.protonapp.view.newtask.CreateTaskActivity
import com.example.protonapp.viewmodel.main.TasksViewModel
import kotlinx.android.synthetic.main.fragmnet_task_list.*
import org.kodein.di.generic.instance

abstract class BaseTasksListFragment : BaseFragment() {

    protected lateinit var viewModel: TasksViewModel
    private lateinit var tasksAdapter: TaskListAdapter
    private val fileUtils: FileUtils by instance()
    private val workManagerUtils: WorkManagerUtils by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = getModel(TasksViewModel::class.java)
        viewModel.viewState.observe(this, Observer {
            tasksAdapter.submitList(it)
            swipeLayout.isRefreshing = false
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View =
            inflater.inflate(R.layout.fragmnet_task_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTaskList()
        swipeLayout.setOnRefreshListener { getTasks() }
        setupSwipes()
        getTasks()
    }

    abstract fun getTasks()

    open fun setupSwipes() {}

    protected fun getSwipeHandler(swipeDirection: Int, delay: Int = 0) =
            TaskSwipeCallback(swipeDirection) { adapterPosition ->
                tasksAdapter.getItemAt(adapterPosition)?.let {
                    when {
                        workManagerUtils.isWorkScheduled(it.id) -> {
                            onIsScheduledOrRunning(adapterPosition, R.string.already_scheduled)
                        }
                        workManagerUtils.isWorkRunning(it.id) -> {
                            onIsScheduledOrRunning(adapterPosition, R.string.already_ongoing)
                        }
                        else -> if (delay == 0) viewModel.startTask(it) else viewModel.scheduleTask(it, delay)
                    }
                }
            }

    private fun setupTaskList() {
        tasksAdapter = TaskListAdapter(fileUtils, workManagerUtils) { selectedTask -> onTaskSelected(selectedTask) }
        recyclerView.adapter = tasksAdapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        val dividerItemDecoration = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    private fun onIsScheduledOrRunning(adapterPosition: Int, stringId: Int) {
        tasksAdapter.notifyItemChanged(adapterPosition)
        showToast(activity, stringId, GENERAL_MESSAGE)
    }

    private fun onTaskSelected(selectedTask: Task) {
        when {
            workManagerUtils.isWorkScheduled(selectedTask.id) ->
                showToast(activity, R.string.cannot_edit_scheduled, GENERAL_ERROR)
            workManagerUtils.isWorkRunning(selectedTask.id) ->
                showToast(activity, R.string.cannot_edit_ongoing, GENERAL_ERROR)
            else -> startCreateTaskActivity(selectedTask)
        }
    }

    private fun startCreateTaskActivity(selectedTask: Task) {
        context?.let {
            it.startActivity {
                component = it.componentFor(CreateTaskActivity::class.java)
                putExtra(CreateTaskActivity.TASK, selectedTask)
            }
        }
    }
}
