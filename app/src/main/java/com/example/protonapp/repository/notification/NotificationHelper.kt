package com.example.protonapp.repository.notification

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.protonapp.R
import com.example.protonapp.view.main.MainActivity

class NotificationHelper(private val context: Context) {

    private val notificationManager = NotificationManagerCompat.from(context)
    private val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    fun createNotification(taskName: String, originName: String): Int {
        notificationId++
        val localNotificationId = notificationId

        notificationBuilder.apply {
            setSmallIcon(R.drawable.ic_notifications_24dp)
            setContentTitle(taskName)
            setContentText(context.getString(R.string.uploading) + " $originName")
            setOnlyAlertOnce(true)
            setProgress(PROGRESS_MAX, PROGRESS_INIT, false)
            setContentIntent(getOpenActivityIntent())
            priority = NotificationCompat.PRIORITY_DEFAULT
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

    private fun getOpenActivityIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        return PendingIntent.getActivity(context, 0, intent, 0)
    }

    companion object {
        @JvmStatic
        var notificationId = 1000
        const val PROGRESS_MAX = 100
        const val PROGRESS_INIT = 0
        const val CHANNEL_ID = "proton_channel_id"
        const val CHANNEL_NAME = "Proton"
        const val CHANNEL_DESCRIPTION = "Proton Channel"
    }
}
