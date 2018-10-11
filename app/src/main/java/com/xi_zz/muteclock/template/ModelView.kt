package com.xi_zz.muteclock.template

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable

interface ModelView {
    fun bind(disposable: CompositeDisposable)
    fun saveInstanceState(outState: Bundle)
    fun restoreInstanceState(savedInstanceState: Bundle)
}
