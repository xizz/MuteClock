package com.xi_zz.devicesilencer.ui.template

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

interface ViewModel<T : Parcelable> {
    val state: Observable<T>
    fun saveState(state: Bundle)
    fun loadState(state: Bundle)
    fun mutateState(newState: T)
    fun clearDisposable()
}

abstract class BaseViewModel<T : Parcelable>(initState: T) : ViewModel<T> {
    private val subject = BehaviorSubject.create<T>().apply { onNext(initState) }
    protected val compositeDisposable = CompositeDisposable()
    protected var currentState = initState

    override val state: Observable<T>
        get() = subject

    override fun saveState(state: Bundle) {
        state.putParcelable(javaClass.simpleName, currentState)
        Log.e("xizz", "saveState: ${javaClass.simpleName}")
        Log.e("xizz", "saveState: ${javaClass.canonicalName}")
        Log.e("xizz", "saveState: ${javaClass.name}")
        Log.e("xizz", "saveState: ${javaClass.typeName}")
    }

    override fun loadState(state: Bundle) {
        mutateState(state.getParcelable(javaClass.simpleName))
    }

    override fun mutateState(newState: T) {
        currentState = newState
        subject.onNext(currentState)
    }

    override fun clearDisposable() {
        compositeDisposable.clear()
    }
}