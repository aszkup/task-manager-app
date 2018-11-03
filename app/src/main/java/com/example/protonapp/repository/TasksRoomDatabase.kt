package com.example.protonapp.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class TasksRoomDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        private var INSTANCE: TasksRoomDatabase? = null

        fun getInstance(context: Context): TasksRoomDatabase? {
            if (INSTANCE == null) {
                synchronized(TasksRoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            TasksRoomDatabase::class.java, "tasks.db")
                            .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
