package com.xi_zz.muteclock.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: MainViewModel

    private val view: MainView by lazy { MainView(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(view)
        view.bindViewModel(viewModel)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        viewModel.loadState(savedInstanceState)
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onDestroy() {
        viewModel.clearDisposable()
        view.clearDisposable()
        super.onDestroy()
    }
}
