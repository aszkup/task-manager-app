package com.example.taskmanagerapp.repository.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.taskmanagerapp.R
import com.example.taskmanagerapp.view.main.MainActivity

class NotificationHelper(private val context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)
    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        initNotificationBuilder()
    }

    fun createNotification(taskId: String, taskName: String, originName: String): Int {
        notificationId++
        val localNotificationId = notificationId
        notificationBuilder.apply {
            setContentTitle(taskName)
            setContentText(context.getString(R.string.uploading) + " $originName")
            addAction(0, context.getString(R.string.cancel), getCancelTaskIntent(taskId, taskName))
            addAction(0, context.getString(R.string.new_task), getCreateTaskIntent())
        }
        notificationManager.notify(localNotificationId, notificationBuilder.build())
        return localNotificationId
    }

    fun updateProgress(progress: Int, notificationId: Int) {
        notificationManager.apply {
            notificationBuilder.apply {
                setProgress(PROGRESS_MAX, progress, false)
                setContentText("$progress %")
                notify(notificationId, build())
            }
        }
    }

    fun markNotificationFinished(notificationId: Int) {
        notificationManager.apply {
            notificationBuilder.apply {
                setContentText(context.getString(R.string.upload_completed))
                setProgress(0, 0, false)
                mActions.clear()
                notify(notificationId, build())
            }
        }
    }

    fun markNotificationCanceled(notificationId: Int) {
        notificationManager.apply {
            notificationBuilder.apply {
                setContentText(context.getString(R.string.upload_canceled))
                setProgress(0, 0, false)
                mActions.clear()
                addAction(0, context.getString(R.string.new_task), getCreateTaskIntent())
                notify(notificationId, build())
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
            description = CHANNEL_DESCRIPTION
        }

        val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun initNotificationBuilder() {
        notificationBuilder.apply {
            setSmallIcon(R.drawable.ic_notifications_24dp)
            setOnlyAlertOnce(true)
            setProgress(PROGRESS_MAX, PROGRESS_INIT, false)
            setContentIntent(getOpenActivityIntent())
            priority = NotificationCompat.PRIORITY_DEFAULT
        }
    }

    private fun getOpenActivityIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    private fun getCancelTaskIntent(taskId: String, taskName: String): PendingIntent {
        val intent = Intent().apply {
            action = context.getString(R.string.cancel_task_action)
            setClass(context, NotificationReceiver::class.java)
            putExtra(TASK_ID, taskId)
            putExtra(TASK_NAME, taskName)
        }
        return PendingIntent.getBroadcast(context, CANCEL_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun getCreateTaskIntent(): PendingIntent {
        val intent = Intent().apply {
            action = context.getString(R.string.create_task_action)
            setClass(context, NotificationReceiver::class.java)
        }
        return PendingIntent.getBroadcast(context, CREATE_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        @JvmStatic
        var notificationId = 1000
        const val PROGRESS_MAX = 100
        const val PROGRESS_INIT = 0
        const val CHANNEL_ID = "taskmanager_channel_id"
        const val TASK_ID = "task_id"
        const val TASK_NAME = "task_name"
        const val CHANNEL_NAME = "TaskManager"
        const val CHANNEL_DESCRIPTION = "TaskManager Channel"
        const val CANCEL_REQUEST_CODE = 100
        const val CREATE_REQUEST_CODE = 101
    }
}
