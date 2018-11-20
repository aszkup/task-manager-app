package com.example.protonapp.repository.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.base.utils.extensions.start
import com.example.protonapp.R
import com.example.protonapp.repository.notification.NotificationHelper.Companion.TASK_ID
import com.example.protonapp.repository.notification.NotificationHelper.Companion.TASK_NAME
import com.example.protonapp.view.newtask.CreateTaskActivity
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        when (intent?.action) {
            context.getString(R.string.cancel_task_action) -> {
                val taskId = intent?.getStringExtra(TASK_ID)
                val taskName = intent?.getStringExtra(TASK_NAME)
                Timber.i("Cancel Task \"$taskName\" intent received")
            }
            context.getString(R.string.create_task_action) -> {
                Timber.i("Create Task intent received")
                context.start<CreateTaskActivity>()
            }
        }
    }
}
