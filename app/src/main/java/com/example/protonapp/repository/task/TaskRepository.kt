package com.example.protonapp.repository.task

import android.app.Application
import com.example.protonapp.repository.TasksRoomDatabase

class TaskRepository(private val application: Application) {

    private var tasksDatabase: TasksRoomDatabase? = TasksRoomDatabase.getInstance(application)
    private var tasksDao = tasksDatabase?.taskDao()

}
