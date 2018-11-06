package com.example.protonapp.repository.task

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class TaskRepository(
        private val tasksDao: TaskDao
) {

    fun store(task: Task): Completable =
            Completable.fromAction {
                tasksDao.insert(task)
            }.subscribeOn(Schedulers.io())

    fun getTasks() = tasksDao.tasks()
            .subscribeOn(Schedulers.io())

    fun getTasksPaged(): Flowable<PagedList<Task>> =
            RxPagedListBuilder(tasksDao.tasksPaged(), PAGE_SIZE)
                    .buildFlowable(BackpressureStrategy.LATEST)
                    .subscribeOn(Schedulers.io())

    fun remove(task: Task): Completable =
            Completable.fromAction {
                tasksDao.delete(task)
            }.subscribeOn(Schedulers.io())

    companion object {
        const val PAGE_SIZE = 20
    }
}
