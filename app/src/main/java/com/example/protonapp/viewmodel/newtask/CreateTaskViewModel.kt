package com.example.protonapp.viewmodel.newtask

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.android.base.model.InProgress
import com.android.base.model.OperationError
import com.android.base.model.ViewState
import com.android.base.utils.BaseMessage
import com.android.base.utils.extensions.applyIoSchedulers
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.R
import com.example.protonapp.model.CreateTaskViewState
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class CreateTaskViewModel(
        private val taskRepository: TaskRepository
) : BaseViewModel() {

    private var task: Task? = null
    private var uri: Uri? = null

    val viewState: MutableLiveData<ViewState<CreateTaskViewState>> = MutableLiveData()

    fun init(task: Task) {
        Timber.d("Init with task: $task")
        this.task = task
        this.uri = Uri.parse(task.fileUri)
        viewState.postValue(ViewState(CreateTaskViewState(task, uri)))
    }

    fun createTask(task: Task) {
        this.task = task
        Timber.d("Create task: ${this.task}")
        taskRepository.store(task.copy(fileUri = uri.toString()))
                .applyIoSchedulers()
                .doOnSubscribe { viewState.value = ViewState(status = InProgress()) }
                .subscribe(::onTaskAdded, ::onInsertError)
                .addTo(disposables)
    }

    fun saveTask(task: Task) {
        this.task = task.copy(id = this.task!!.id, fileUri = uri.toString())
        Timber.d("Update task: ${this.task}")
        taskRepository.store(this.task!!)
                .applyIoSchedulers()
                .doOnSubscribe { viewState.value = ViewState(status = InProgress()) }
                .subscribe(::onTaskAdded, ::onInsertError)
                .addTo(disposables)
    }

    fun storeSelectedFileUri(fileUri: Uri) {
        uri = fileUri
        viewState.postValue(ViewState(CreateTaskViewState(task, uri)))
    }

    private fun onTaskAdded() {
        Timber.i("Task stored: ${task?.name}")
        viewState.postValue(ViewState(CreateTaskViewState(task, uri, taskStored = true)))
    }

    private fun onInsertError(throwable: Throwable) {
        Timber.w("Insertion error: $task")
        Timber.w(throwable)
        viewState.postValue(ViewState(CreateTaskViewState(task, uri, taskStored = false),
                status = OperationError(BaseMessage(R.string.create_task_failed))))
    }
}
