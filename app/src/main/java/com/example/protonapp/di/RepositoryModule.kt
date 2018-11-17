package com.example.protonapp.di

import androidx.work.WorkManager
import com.example.protonapp.repository.AppDatabase
import com.example.protonapp.repository.notification.NotificationUtils
import com.example.protonapp.repository.task.TaskDao
import com.example.protonapp.repository.task.TaskRepository
import com.example.protonapp.utils.FileUtils
import com.example.protonapp.utils.WorkManagerUtils
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val repositoryModule = Kodein.Module {
    bind<AppDatabase>() with singleton { AppDatabase.getInstance(instance()) }
    bind<TaskDao>() with singleton { (instance() as AppDatabase).taskDao() }
    bind<TaskRepository>() with singleton { TaskRepository(instance()) }

    bind<WorkManager>() with singleton { WorkManager.getInstance() }
    bind<WorkManagerUtils>() with singleton { WorkManagerUtils(instance()) }

    bind<FileUtils>() with singleton { FileUtils(instance()) }
    bind<NotificationUtils>() with singleton { NotificationUtils(instance()) }
}
