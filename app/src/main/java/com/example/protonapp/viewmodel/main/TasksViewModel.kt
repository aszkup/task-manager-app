package com.example.protonapp.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import com.example.protonapp.utils.WorkManagerUtils
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class TasksViewModel(
        private val taskRepository: TaskRepository,
        private val workManagerUtils: WorkManagerUtils
) : BaseViewModel() {

    val viewState: MutableLiveData<PagedList<Task>> = MutableLiveData()

    fun getPendingTasks() {
        Timber.d("Get pending tasks")
        taskRepository.getPendingTasksPaged()
                .subscribe({ viewState.value = it }, { Timber.e(it) })
                .addTo(disposables)
    }

    fun getFinishedTasks() {
        Timber.d("Get finished tasks")
        taskRepository.getFinishedTasksPaged()
                .subscribe({ viewState.value = it }, { Timber.e(it) })
                .addTo(disposables)
    }

    fun startTask(task: Task, delay: Int) {
        Timber.i("Schedule task: `${task.name}` with delay: $delay")
        Timber.d("Schedule task: $task")
        taskRepository.scheduleTask(task)
                .subscribe({ workManagerUtils.startWorker(task, delay) }, { Timber.e(it) })
                .addTo(disposables)
    }
}
