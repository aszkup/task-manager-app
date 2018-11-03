package com.example.protonapp.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.protonapp.repository.task.Task
import com.example.protonapp.repository.task.TaskDao

@Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        fun getInstance(context: Context): AppDatabase {
            synchronized(AppDatabase::class) {
                return Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "tasks.db")
                        .build()
            }
        }
    }
}
