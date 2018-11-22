package com.example.protonapp.utils

import androidx.work.*
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.worker.UploadFileWorker
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WorkManagerUtils(private val workManager: WorkManager) {

    fun isWorkScheduled(tag: String): Boolean {
        return isInState(tag, WorkInfo.State.ENQUEUED)
    }

    fun isWorkRunning(tag: String): Boolean {
        return isInState(tag, WorkInfo.State.RUNNING)
    }

    fun startWorker(task: Task, delay: Int) {
        val constraints = Constraints.Builder().apply {
            setRequiresCharging(false)
            setRequiredNetworkType(NetworkType.CONNECTED)
        }.build()
        val data = Data.Builder().apply { putString(UploadFileWorker.TASK_ID, task.id) }.build()
        val work = OneTimeWorkRequest.Builder(UploadFileWorker::class.java).apply {
            setInitialDelay(delay.toLong(), TimeUnit.SECONDS)
            setConstraints(constraints)
            setInputData(data)
            addTag(task.id)
        }.build()
        workManager.enqueue(work)
    }

    private fun isInState(tag: String, expectedState: WorkInfo.State): Boolean {
        return try {
            workManager.getWorkInfosByTag(tag).get().forEach { workInfo ->
                val state = workInfo.state
                if (state == expectedState) {
                    return true
                }
            }
            false
        } catch (exception: Exception) {
            Timber.w(exception)
            false
        }
    }
}
