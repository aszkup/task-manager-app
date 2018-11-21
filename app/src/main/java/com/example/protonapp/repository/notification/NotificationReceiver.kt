package com.example.protonapp.repository.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.android.base.utils.extensions.componentFor
import com.android.base.utils.extensions.startActivity
import com.example.protonapp.ProtonApplication
import com.example.protonapp.R
import com.example.protonapp.repository.notification.NotificationHelper.Companion.TASK_ID
import com.example.protonapp.repository.notification.NotificationHelper.Companion.TASK_NAME
import com.example.protonapp.view.newtask.CreateTaskActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver(), KodeinAware {

    override lateinit var kodein: Kodein

    override fun onReceive(context: Context, intent: Intent?) {
        kodein = (context.applicationContext as ProtonApplication).kodein
        when (intent?.action) {
            context.getString(R.string.cancel_task_action) -> {
                val taskId = intent?.getStringExtra(TASK_ID)
                val taskName = intent?.getStringExtra(TASK_NAME)
                Timber.i("Cancel Task \"$taskName\" intent received")
            }
            context.getString(R.string.create_task_action) -> {
                Timber.i("Create Task intent received")
                context.startActivity {
                    component = context.componentFor(CreateTaskActivity::class.java)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
            }
        }
    }
}
