package com.example.protonapp.repository.task

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Flowable


@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun tasks(): Flowable<Task>

    @Query("SELECT * FROM tasks ORDER BY startedAt DESC, createdAt DESC")
    fun tasksPaged(): DataSource.Factory<Int, Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task: Task)
}
