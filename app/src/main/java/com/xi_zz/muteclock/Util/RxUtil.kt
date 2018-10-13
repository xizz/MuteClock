package com.xi_zz.muteclock.Util

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.Optional

val <T>Optional<T>.value: T? get() = if (isPresent) get() else null

fun <T> Observable<Optional<T>>.unwrap(): Observable<T> = this.filter { it.value != null }.map { it.value }

fun <T, R> Observable<T>.unwrapMap(mapper: (T) -> R?): Observable<R> =
    map {
        mapper(it)?.let { r -> Optional.of(r) } ?: Optional.empty()
    }.unwrap()

fun <T> Observable<T>.subscribeFromUI(onNext: (T) -> Unit): Disposable =
    distinctUntilChanged()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            onNext(it)
        }
