package com.example.protonapp.viewmodel.newtask

import androidx.lifecycle.MutableLiveData
import com.android.base.model.InProgress
import com.android.base.model.ViewState
import com.android.base.utils.extensions.applyIoSchedulers
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.model.CreateTaskViewState
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class CreateTaskViewModel(
        private val taskRepository: TaskRepository
) : BaseViewModel() {

    private var task: Task? = null

    val viewState: MutableLiveData<ViewState<CreateTaskViewState>> = MutableLiveData()

    fun createTask(task: Task) {
        this.task = task
        taskRepository.insert(task)
                .applyIoSchedulers()
                .doOnSubscribe { viewState.value = ViewState(status = InProgress()) }
                .subscribe(::onTaskAdded, ::onInsertError)
                .addTo(disposables)
    }

    private fun onTaskAdded() {
        Timber.i("Task stored: $task")
        viewState.postValue(ViewState(CreateTaskViewState(task, taskStored = true)))
    }

    private fun onInsertError(throwable: Throwable) {
        Timber.w("Insertion error: $task")
        Timber.w(throwable)
        viewState.postValue(ViewState(CreateTaskViewState(task, taskStored = false)))
    }
}
