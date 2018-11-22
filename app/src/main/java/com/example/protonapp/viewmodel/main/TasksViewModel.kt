package com.example.protonapp.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import com.example.protonapp.utils.WorkManagerUtils
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

/**
 * Task [ViewModel]
 */
class TasksViewModel(
        private val taskRepository: TaskRepository,
        private val workManagerUtils: WorkManagerUtils
) : BaseViewModel() {

    val viewState: MutableLiveData<PagedList<Task>> = MutableLiveData()

    /**
     * Get pending tasks
     */
    fun getPendingTasks() {
        Timber.i("Get pending tasks")
        taskRepository.getPendingTasksPaged()
                .subscribe({ viewState.value = it }, { Timber.e(it) })
                .addTo(disposables)
    }

    /**
     * Get finished tasks
     */
    fun getFinishedTasks() {
        Timber.i("Get finished tasks")
        taskRepository.getFinishedTasksPaged()
                .subscribe({ viewState.value = it }, { Timber.e(it) })
                .addTo(disposables)
    }

    /**
     * Start task
     * @param task [Task]
     */
    fun startTask(task: Task) {
        Timber.i("Start task: `${task.name}`")
        Timber.d("Start task: $task")
        workManagerUtils.startWorker(task)
    }

    /**
     * Schedule task
     * @param task [Task]
     * @param delay [Int]
     */
    fun scheduleTask(task: Task, delay: Int) {
        Timber.i("Schedule task: `${task.name}` with delay: $delay")
        Timber.d("Schedule task: $task")
        taskRepository.scheduleTask(task)
                .subscribe({ workManagerUtils.startWorker(task, delay) }, { Timber.e(it) })
                .addTo(disposables)
    }
}
