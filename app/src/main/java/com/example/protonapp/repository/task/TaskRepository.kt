package com.example.protonapp.repository.task

import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import org.threeten.bp.Instant

class TaskRepository(
        private val tasksDao: TaskDao
) {

    fun store(task: Task): Completable =
            Completable.fromAction {
                tasksDao.insert(task)
            }

    fun getTask(id: String) = tasksDao.taskWithId(id)

    fun getTasks() = tasksDao.tasks()

    fun getTasksPaged(): Flowable<PagedList<Task>> =
            RxPagedListBuilder(tasksDao.tasksPaged(), PAGE_SIZE)
                    .buildFlowable(BackpressureStrategy.LATEST)

    fun getPendingTasksPaged(): Flowable<PagedList<Task>> =
            RxPagedListBuilder(tasksDao.pendingTasksPaged(), PAGE_SIZE)
                    .buildFlowable(BackpressureStrategy.LATEST)

    fun getFinishedTasksPaged(): Flowable<PagedList<Task>> =
            RxPagedListBuilder(tasksDao.finishedTasksPaged(), PAGE_SIZE)
                    .buildFlowable(BackpressureStrategy.LATEST)

    fun scheduleTask(task: Task): Completable =
            Completable.fromAction {
                tasksDao.update(task.copy(scheduledAt = Instant.now(), startedAt = null, finishedAt = null))
            }

    fun startTask(task: Task): Single<Task> =
            Single.just(task)
                    .map { task.copy(startedAt = Instant.now(), finishedAt = null) }
                    .doOnSuccess { tasksDao.update(it) }

    fun finishTask(task: Task): Single<Task> =
            Single.just(task)
                    .map { task.copy(finishedAt = Instant.now()) }
                    .doOnSuccess { tasksDao.update(it) }

    fun cancelTask(task: Task): Completable =
            Completable.fromAction {
                tasksDao.update(task.copy(scheduledAt = null, startedAt = null, finishedAt = null))
            }

    fun remove(task: Task): Completable =
            Completable.fromAction {
                tasksDao.delete(task)
            }

    companion object {
        const val PAGE_SIZE = 20
    }
}
