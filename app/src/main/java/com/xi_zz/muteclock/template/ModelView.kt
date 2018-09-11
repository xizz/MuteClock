package com.xi_zz.devicesilencer.ui.template

import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable

interface ModelView {
    fun bind(disposable: CompositeDisposable)
    fun saveInstanceState(outState: Bundle)
    fun restoreInstanceState(savedInstanceState: Bundle)
}
