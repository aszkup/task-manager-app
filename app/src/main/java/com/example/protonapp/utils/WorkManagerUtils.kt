package com.example.protonapp.utils

import androidx.work.WorkInfo
import androidx.work.WorkManager
import timber.log.Timber

class WorkManagerUtils(private val workManager: WorkManager) {

    fun isWorkScheduled(tag: String): Boolean {
        return isInState(tag, WorkInfo.State.ENQUEUED)
    }

    fun isWorkRunning(tag: String): Boolean {
        return isInState(tag, WorkInfo.State.RUNNING)
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
