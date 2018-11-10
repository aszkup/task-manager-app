package com.example.protonapp.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class FinishedTasksViewModel(
        private val taskRepository: TaskRepository
) : BaseViewModel() {

    val viewState: MutableLiveData<PagedList<Task>> = MutableLiveData()

    fun getFinishedTasks() {
        Timber.d("Get finished tasks")
        taskRepository.getFinishedTasksPaged()
                .subscribe({ viewState.value = it }, { Timber.e(it) })
                .addTo(disposables)
    }
}
