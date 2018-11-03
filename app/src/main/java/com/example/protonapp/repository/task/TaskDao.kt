package com.example.protonapp.repository.task

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface TaskDao {

    @Insert
    fun insert(task: Task)
}
