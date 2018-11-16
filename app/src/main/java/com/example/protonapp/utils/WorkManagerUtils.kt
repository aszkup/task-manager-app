package com.example.protonapp.utils

import androidx.work.WorkInfo
import androidx.work.WorkManager
import timber.log.Timber
import java.util.concurrent.ExecutionException

class WorkManagerUtils(private val workManager: WorkManager) {

    fun isWorkScheduled(tag: String): Boolean {
        val statuses = workManager.getWorkInfosByTag(tag)
        return try {
            var running = false
            val workInfoList = statuses.get()
            for (workInfo in workInfoList) {
                val state = workInfo.state
                running = (state == WorkInfo.State.RUNNING) or (state == WorkInfo.State.ENQUEUED)
            }
            running
        } catch (exception: ExecutionException) {
            Timber.w(exception)
            false
        } catch (exception: InterruptedException) {
            Timber.w(exception)
            false
        }
    }
}
