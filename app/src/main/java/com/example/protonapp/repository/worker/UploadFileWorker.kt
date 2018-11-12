package com.example.protonapp.repository.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.protonapp.repository.task.TaskRepository
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import io.reactivex.disposables.Disposable
import timber.log.Timber

class UploadFileWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    private val injector = KodeinInjector()

    private val repository: TaskRepository by injector.instance()
    private var getTaskDisposable: Disposable? = null

    override fun doWork(): Result {
        injector.inject((applicationContext as KodeinAware).kodein)
        Timber.i("Worker ${this::class.java.simpleName} started.")

        inputData.getString(TASK_ID)?.let { taskId ->
            getTaskDisposable = repository.getTask(taskId)
                    .subscribe({ Timber.d("Task to process $it") }, { Timber.e(it) })
            return ListenableWorker.Result.SUCCESS
        }
        return ListenableWorker.Result.FAILURE
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}
