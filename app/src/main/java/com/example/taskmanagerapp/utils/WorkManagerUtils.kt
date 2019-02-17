package com.example.taskmanagerapp.utils

import androidx.work.*
import com.example.taskmanagerapp.repository.worker.UploadFileWorker
import timber.log.Timber
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

class WorkManagerUtils(private val workManager: WorkManager) {

    fun isWorkScheduled(tag: String): Boolean {
        return isInState(tag, WorkInfo.State.ENQUEUED)
    }

    fun isWorkRunning(tag: String): Boolean {
        return isInState(tag, WorkInfo.State.RUNNING)
    }

    fun startWorker(tag: String, delay: Int = 0) {
        val constraints = Constraints.Builder().apply {
            setRequiresCharging(false)
            setRequiredNetworkType(NetworkType.CONNECTED)
        }.build()
        val data = Data.Builder().apply { putString(WORK_REQUEST_TAG, tag) }.build()
        val work = OneTimeWorkRequest.Builder(UploadFileWorker::class.java).apply {
            setInitialDelay(delay.toLong(), TimeUnit.SECONDS)
            setConstraints(constraints)
            setInputData(data)
            addTag(tag)
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
        } catch (exception: ExecutionException) {
            Timber.w(exception)
            false
        } catch (exception: InterruptedException) {
            Timber.w(exception)
            false
        }
    }

    companion object {
        const val WORK_REQUEST_TAG = "work_request_tag"
    }
}
