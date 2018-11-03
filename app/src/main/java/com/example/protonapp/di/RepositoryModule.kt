package com.example.protonapp.di

import com.example.protonapp.repository.AppDatabase
import com.example.protonapp.repository.task.TaskDao
import com.example.protonapp.repository.task.TaskRepository
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton

val repositoryModule = Kodein.Module {
    bind<AppDatabase>() with singleton { AppDatabase.getInstance(kodein.instance()) }
    bind<TaskDao>() with singleton { (kodein.instance() as AppDatabase).taskDao() }
    bind<TaskRepository>() with singleton { TaskRepository(kodein.instance()) }
}
