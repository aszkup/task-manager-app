package com.example.protonapp.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class PendingTasksViewModel(
        private val taskRepository: TaskRepository
) : BaseViewModel() {

    val viewState: MutableLiveData<PagedList<Task>> = MutableLiveData()

    fun getTasks() {
        Timber.d("Get tasks")
        taskRepository.getTasksPaged()
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
