package com.xi_zz.muteclock.ui

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.FrameLayout
import com.xi_zz.muteclock.R
import com.xi_zz.muteclock.template.ModelView
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class MainView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ModelView {

    @Inject
    lateinit var viewModel: MainViewModel
//    val viewModel = MainViewModel().apply { Log.d("xizz", "New view model created") }

    init {
        inflate(context, R.layout.main, this)
        setPadding(16, 16, 16, 16)
        setupView()
    }

    fun setupView() {
    }

    override fun bind(disposable: CompositeDisposable) {
    }

    override fun saveInstanceState(outState: Bundle) {
//        viewModel.saveState(outState)
    }

    override fun restoreInstanceState(savedInstanceState: Bundle) {
//        viewModel.loadState(savedInstanceState)
    }
}