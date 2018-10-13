package com.xi_zz.muteclock.ui

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.xi_zz.muteclock.R
import com.xi_zz.muteclock.Util.subscribeFromUI
import com.xi_zz.muteclock.template.BaseModelView
import com.xi_zz.muteclock.template.ModelView
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.main.view.cancelEndTime
import kotlinx.android.synthetic.main.main.view.cancelStartTime
import kotlinx.android.synthetic.main.main.view.endTimeText
import kotlinx.android.synthetic.main.main.view.setEndTimeButton
import kotlinx.android.synthetic.main.main.view.setStartTimeButton
import kotlinx.android.synthetic.main.main.view.startTimeText
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class MainView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ModelView by BaseModelView() {

    init {
        inflate(context, R.layout.main, this)
        setPadding(16, 16, 16, 16)
    }

    lateinit var viewModel: MainViewModel

    fun setupView() {
        viewModel.state.subscribeFromUI {
            startTimeText.text = it.startTime?.toString() ?: context.getString(R.string.no_start_time)
            endTimeText.text = it.endTime?.toString() ?: context.getString(R.string.no_end_time)
        }.addTo(compositeDisposable)

        setStartTimeButton.setOnClickListener { viewModel.startTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES) }
        setEndTimeButton.setOnClickListener { viewModel.endTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES) }
        cancelStartTime.setOnClickListener { viewModel.startTime = null }
        cancelEndTime.setOnClickListener { viewModel.endTime = null }
    }


}