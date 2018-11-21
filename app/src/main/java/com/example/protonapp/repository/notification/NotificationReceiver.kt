package com.example.protonapp.repository.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.WorkManager
import com.android.base.utils.extensions.componentFor
import com.android.base.utils.extensions.startActivity
import com.example.protonapp.ProtonApplication
import com.example.protonapp.R
import com.example.protonapp.repository.notification.NotificationHelper.Companion.TASK_ID
import com.example.protonapp.repository.notification.NotificationHelper.Companion.TASK_NAME
import com.example.protonapp.view.newtask.CreateTaskActivity
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import timber.log.Timber

class NotificationReceiver : BroadcastReceiver(), KodeinAware {

    override lateinit var kodein: Kodein
    private val workManager: WorkManager by instance()
    private lateinit var context: Context

    override fun onReceive(context: Context, intent: Intent?) {
        this.context = context
        kodein = (context.applicationContext as ProtonApplication).kodein
        intent?.let {
            when (intent.action) {
                context.getString(R.string.cancel_task_action) -> onCancelIntent(intent)
                context.getString(R.string.create_task_action) -> onCreateTaskIntent()
            }
        }
    }

    private fun onCancelIntent(intent: Intent) {
        val taskName = intent.getStringExtra(TASK_NAME)
        Timber.i("Cancel Task \"$taskName\" intent received")
        intent.getStringExtra(TASK_ID)?.let {
            workManager.cancelAllWorkByTag(it)
        }
    }

    private fun onCreateTaskIntent() {
        Timber.i("Create Task intent received")
        context.startActivity {
            component = context.componentFor(CreateTaskActivity::class.java)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }
}
