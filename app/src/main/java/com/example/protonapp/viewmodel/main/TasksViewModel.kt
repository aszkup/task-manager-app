package com.example.protonapp.viewmodel.main

import androidx.lifecycle.MutableLiveData
import androidx.paging.PagedList
import androidx.work.*
import com.android.base.viewmodel.BaseViewModel
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskRepository
import com.example.protonapp.repository.worker.UploadFileWorker
import com.example.protonapp.repository.worker.UploadFileWorker.Companion.TASK_ID
import io.reactivex.rxkotlin.addTo
import timber.log.Timber
import java.util.concurrent.TimeUnit

class TasksViewModel(
        private val taskRepository: TaskRepository,
        private val workManager: WorkManager
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
        Timber.i("Start task: ${task.name} with delay: $delay")
        Timber.d("Start task: $task")
        taskRepository.startTask(task)
                .subscribe({ startWorker(task, delay) }, { Timber.e(it) })
                .addTo(disposables)
    }

    fun finishTask(task: Task) {
        Timber.d("Finish task: $task")
        taskRepository.finishTask(task)
                .subscribe({ }, { Timber.e(it) })
                .addTo(disposables)
    }

    private fun startWorker(task: Task, delay: Int) {
        val constraints = Constraints.Builder().apply {
            setRequiresCharging(false)
            setRequiredNetworkType(NetworkType.CONNECTED)
        }.build()
        val data = Data.Builder().apply { putString(TASK_ID, task.id) }.build()
        val work = OneTimeWorkRequest.Builder(UploadFileWorker::class.java).apply {
            setInitialDelay(delay.toLong(), TimeUnit.SECONDS)
            setConstraints(constraints)
            setInputData(data)
        }.build()
        workManager.enqueue(work)
    }
}
