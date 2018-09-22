package com.xi_zz.muteclock.Util

import io.reactivex.Observable
import java.util.Optional

val <T>Optional<T>.value: T? get() = if (isPresent) get() else null

fun <T> Observable<Optional<T>>.unwrap(): Observable<T> = this.filter { it.value != null }.map { it.value }

fun <T, R> Observable<T>.unwrapMap(mapper: (T) -> R?): Observable<R> =
        map {
            mapper(it)?.let { r -> Optional.of(r) } ?: Optional.empty()
        }.unwrap()