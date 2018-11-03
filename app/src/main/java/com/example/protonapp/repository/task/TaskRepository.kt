package com.example.protonapp.repository.task

import io.reactivex.Completable

class TaskRepository(
        private val tasksDao: TaskDao
) {

    fun insert(task: Task): Completable {
        return Completable.fromAction {
            tasksDao.insert(task)
        }
    }
}
