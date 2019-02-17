package com.example.taskmanagerapp.viewmodel.newtask

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.android.base.model.*
import com.android.base.utils.BaseMessage
import com.android.base.utils.Event
import com.android.base.utils.extensions.applyIoSchedulers
import com.android.base.viewmodel.BaseViewModel
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.model.CreateTaskViewState
import com.example.taskmanagerapp.repository.task.Task
import com.example.taskmanagerapp.repository.task.TaskRepository
import io.reactivex.rxkotlin.addTo
import timber.log.Timber

class CreateTaskViewModel(
        private val taskRepository: TaskRepository
) : BaseViewModel() {

    private var task: Task? = null
    private var uri: Uri? = null

    val taskStoredStatus = MutableLiveData<Event<OperationStatus>>()
    val viewState = MutableLiveData<ViewState<CreateTaskViewState>>()

    fun init(task: Task) {
        Timber.d("Init with task: $task")
        this.task = task
        this.uri = Uri.parse(task.fileUri)
        viewState.postValue(ViewState(CreateTaskViewState(task, uri)))
    }

    fun createTask(task: Task) {
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
        viewState.postValue(ViewState(CreateTaskViewState(task, uri)))
        if (task == null) {
            taskStoredStatus.postValue(Event(Success(BaseMessage(R.string.task_stored))))
        } else {
            taskStoredStatus.postValue(Event(Success(BaseMessage(R.string.task_updated))))
        }
    }

    private fun onInsertError(throwable: Throwable) {
        Timber.w("Insertion error: $task")
        Timber.w(throwable)
        viewState.postValue(ViewState(CreateTaskViewState(task, uri)))
        taskStoredStatus.postValue(Event(OperationError(BaseMessage(R.string.create_task_failed))))
    }
}
