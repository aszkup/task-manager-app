package com.example.protonapp.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class TasksViewModel(
        private val taskRepository: TaskRepository
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

    fun startTask(task: Task) {
        Timber.d("Start task: $task")
        taskRepository.startTask(task)
                .subscribe({ }, { Timber.e(it) })
                .addTo(disposables)
    }
}
