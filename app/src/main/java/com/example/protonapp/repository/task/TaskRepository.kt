package com.example.protonapp.repository.task

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.Instant

class TaskRepository(
        private val tasksDao: TaskDao
) {

    fun store(task: Task): Completable =
            Completable.fromAction {
                tasksDao.insert(task)
            }.subscribeOn(Schedulers.io())

    fun getTask(id: String): Maybe<Task> =
            tasksDao.taskWithId(id)
                    .subscribeOn(Schedulers.io())

    fun getTasks() = tasksDao.tasks()
            .subscribeOn(Schedulers.io())

    fun getTasksPaged(): Flowable<PagedList<Task>> =
            RxPagedListBuilder(tasksDao.tasksPaged(), PAGE_SIZE)
                    .buildFlowable(BackpressureStrategy.LATEST)
                    .subscribeOn(Schedulers.io())

    fun getPendingTasksPaged(): Flowable<PagedList<Task>> =
            RxPagedListBuilder(tasksDao.pendingTasksPaged(), PAGE_SIZE)
                    .buildFlowable(BackpressureStrategy.LATEST)
                    .subscribeOn(Schedulers.io())

    fun getFinishedTasksPaged(): Flowable<PagedList<Task>> =
            RxPagedListBuilder(tasksDao.finishedTasksPaged(), PAGE_SIZE)
                    .buildFlowable(BackpressureStrategy.LATEST)
                    .subscribeOn(Schedulers.io())

    fun scheduleTask(task: Task): Completable =
            Completable.fromAction {
                tasksDao.update(task.copy(scheduledAt = Instant.now(), startedAt = null, finishedAt = null))
            }.subscribeOn(Schedulers.io())

    fun startTask(task: Task): Completable =
            Completable.fromAction {
                tasksDao.update(task.copy(startedAt = Instant.now(), finishedAt = null))
            }.subscribeOn(Schedulers.io())

    fun finishTask(task: Task): Completable =
            Completable.fromAction {
                tasksDao.update(task.copy(finishedAt = Instant.now()))
            }.subscribeOn(Schedulers.io())

    fun remove(task: Task): Completable =
            Completable.fromAction {
                tasksDao.delete(task)
            }.subscribeOn(Schedulers.io())

    companion object {
        const val PAGE_SIZE = 20
    }
}
