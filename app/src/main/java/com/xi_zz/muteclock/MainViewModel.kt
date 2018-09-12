package com.xi_zz.muteclock

import android.os.Parcelable
import com.xi_zz.devicesilencer.ui.template.BaseViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.parcel.Parcelize
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

@Parcelize
data class State(
        var startTime: LocalTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES),
        var endTime: LocalTime = LocalTime.now().truncatedTo(ChronoUnit.MINUTES)
) : Parcelable

class MainViewModel @Inject constructor(val timeService: TimeService) : BaseViewModel<State>(State()) {

    init {
        timeService.observeStartTime().subscribe {
            currentState.startTime = it
            mutateState(currentState)
        }.addTo(compositeDisposable)
        timeService.observeEndTime().subscribe {
            currentState.endTime = it
            mutateState(currentState)
        }.addTo(compositeDisposable)
    }

    var startTime: LocalTime
        get() = currentState.startTime
        set(value) = timeService.setStartTime(value)

    var endTime: LocalTime
        get() = currentState.endTime
        set(value) = timeService.setEndTime(value)
}