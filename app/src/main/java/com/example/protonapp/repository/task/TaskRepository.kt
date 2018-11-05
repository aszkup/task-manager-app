package com.example.protonapp.repository.task

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable

class TaskRepository(
        private val tasksDao: TaskDao
) {

    fun store(task: Task): Completable = Completable.fromAction {
        tasksDao.insert(task)
    }

    fun getTasks() = tasksDao.tasks()

    fun getTasksPaged(): Flowable<PagedList<Task>> =
            RxPagedListBuilder(tasksDao.tasksPaged(), PAGE_SIZE)
                    .buildFlowable(BackpressureStrategy.LATEST)

    companion object {
        const val PAGE_SIZE = 20
    }
}
