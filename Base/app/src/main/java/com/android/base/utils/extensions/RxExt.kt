package com.android.base.utils.extensions

import io.reactivex.*
import io.reactivex.schedulers.Schedulers

/**
 * Apply Io Schedulers
 */
fun <T> Observable<T>.applyIoSchedulers(): Observable<T> =
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())

/**
 * Apply Io Schedulers
 */
fun <T> Single<T>.applyIoSchedulers(): Single<T> =
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())

/**
 * Apply Io Schedulers
 */
fun <T> Maybe<T>.applyIoSchedulers(): Maybe<T> =
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())


/**
 * Apply Io Schedulers
 */
fun Completable.applyIoSchedulers(): Completable =
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())

fun <T> Flowable<T>.applyIoSchedulers(): Flowable<T> =
        subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())