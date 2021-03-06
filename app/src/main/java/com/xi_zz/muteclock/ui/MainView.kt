package com.xi_zz.muteclock.ui

import android.app.TimePickerDialog
import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.TimePicker
import com.xi_zz.muteclock.R
import com.xi_zz.muteclock.Util.subscribeFromUI
import com.xi_zz.muteclock.template.ModelView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.main.view.cancelEndTimeButton
import kotlinx.android.synthetic.main.main.view.cancelStartTimeButton
import kotlinx.android.synthetic.main.main.view.endTimeText
import kotlinx.android.synthetic.main.main.view.setEndTimeButton
import kotlinx.android.synthetic.main.main.view.setStartTimeButton
import kotlinx.android.synthetic.main.main.view.startTimeText
import java.time.LocalTime

class MainView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), ModelView<State, MainViewModel> {

    init {
        inflate(context, R.layout.main, this)
        setPadding(16, 16, 16, 16)
    }

    private val compositeDisposable = CompositeDisposable()
    private lateinit var model: MainViewModel

    private val startTimePickerDialog by lazy {
        val onTimeSetListener: (view: TimePicker, hour: Int, minute: Int) -> Unit = { view, hour, minute ->
            model.startTime = LocalTime.of(hour, minute)
        }
        TimePickerDialog(context, onTimeSetListener, 0, 0, false)
    }

    private val endTimePickerDialog by lazy {
        val onTimeSetListener: (view: TimePicker, hour: Int, minute: Int) -> Unit = { view, hour, minute ->
            model.endTime = LocalTime.of(hour, minute)
        }
        TimePickerDialog(context, onTimeSetListener, 0, 0, false)
    }

    override fun bindViewModel(viewModel: MainViewModel) {
        model = viewModel

        model.state.subscribeFromUI {
            startTimeText.text = it.startTime?.toString() ?: context.getString(R.string.no_start_time)
            endTimeText.text = it.endTime?.toString() ?: context.getString(R.string.no_end_time)
        }.addTo(compositeDisposable)

        setStartTimeButton.setOnClickListener {
            val time = model.currentState.startTime ?: LocalTime.now()
            startTimePickerDialog.updateTime(time.hour, time.minute)
            startTimePickerDialog.show()
        }
        setEndTimeButton.setOnClickListener {
            val time = model.currentState.endTime ?: LocalTime.now()
            endTimePickerDialog.updateTime(time.hour, time.minute)
            endTimePickerDialog.show()
        }
        cancelStartTimeButton.setOnClickListener { model.startTime = null }
        cancelEndTimeButton.setOnClickListener { model.endTime = null }
    }

    override fun clearDisposable() {
        compositeDisposable.clear()
    }

}