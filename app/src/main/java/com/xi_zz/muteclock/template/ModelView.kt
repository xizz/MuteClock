package com.xi_zz.muteclock.template

import android.os.Parcelable

interface ModelView<T : Parcelable, M : ViewModel<T>> {
    fun bindViewModel(viewModel: M)
    fun clearDisposable()
}