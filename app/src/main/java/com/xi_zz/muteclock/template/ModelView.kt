package com.xi_zz.muteclock.template

import io.reactivex.disposables.CompositeDisposable

interface ModelView {
    val compositeDisposable: CompositeDisposable
    fun clearDisposable()
}

class BaseModelView : ModelView {

    override val compositeDisposable = CompositeDisposable()

    override fun clearDisposable() =
        compositeDisposable.clear()
}
