package com.example.protonapp.repository.task

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Flowable


@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun tasks(): Flowable<Task>

    @Query("SELECT * FROM tasks")
    fun tasksPaged(): DataSource.Factory<Int, Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)
}
