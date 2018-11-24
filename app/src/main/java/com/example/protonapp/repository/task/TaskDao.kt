package com.example.protonapp.repository.task

import androidx.paging.DataSource
import androidx.room.*
import io.reactivex.Single


@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE taskid is :id")
    fun taskWithId(id: String): Single<Task>

    @Query("SELECT * FROM tasks")
    fun tasks(): Single<List<Task>>

    @Query("SELECT * FROM tasks ORDER BY startedAt DESC, createdAt DESC")
    fun tasksPaged(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE finishedAt IS NULL ORDER BY startedAt DESC, createdAt DESC")
    fun pendingTasksPaged(): DataSource.Factory<Int, Task>

    @Query("SELECT * FROM tasks WHERE finishedAt IS NOT NULL ORDER BY startedAt DESC, createdAt DESC")
    fun finishedTasksPaged(): DataSource.Factory<Int, Task>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Delete
    fun delete(task: Task)

    @Update
    fun update(task: Task)
}
