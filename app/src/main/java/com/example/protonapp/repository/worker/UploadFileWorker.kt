package com.example.protonapp.repository.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.protonapp.repository.task.TaskRepository
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.KodeinInjector
import com.github.salomonbrys.kodein.instance
import timber.log.Timber

class UploadFileWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    private val injector = KodeinInjector()

    private val repository: TaskRepository by injector.instance()

    override fun doWork(): Result {
        injector.inject((applicationContext as KodeinAware).kodein)
        Timber.i("Worker ${this::class.java.simpleName} started.")

        val taskId = inputData.getString(TASK_ID)
        taskId?.let {
            return ListenableWorker.Result.SUCCESS
        }
        return ListenableWorker.Result.FAILURE
    }

    companion object {
        const val TASK_ID = "task_id"
    }
}
