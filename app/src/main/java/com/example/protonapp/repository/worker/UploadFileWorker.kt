package com.example.protonapp.repository.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import timber.log.Timber

class UploadFileWorker(
        context: Context,
        workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        Timber.i("Worker ${this::class.java.simpleName} started.")
        return ListenableWorker.Result.SUCCESS
    }
}
