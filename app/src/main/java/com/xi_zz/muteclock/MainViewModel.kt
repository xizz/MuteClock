package com.xi_zz.muteclock

import android.os.Parcelable
import com.xi_zz.muteclock.Util.value
import com.xi_zz.muteclock.template.BaseViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.parcel.Parcelize
import java.time.LocalTime
import javax.inject.Inject

@Parcelize
data class State(
        var startTime: LocalTime? = null,
        var endTime: LocalTime? = null
) : Parcelable

class MainViewModel @Inject constructor(val timeService: TimeService) : BaseViewModel<State>(State()) {

    init {
        timeService.observeStartTime().subscribe {
            currentState.startTime = it.value
            mutateState(currentState)
        }.addTo(compositeDisposable)
        timeService.observeEndTime().subscribe {
            currentState.endTime = it.value
            mutateState(currentState)
        }.addTo(compositeDisposable)
    }

    var startTime: LocalTime?
        get() = currentState.startTime
        set(value) = timeService.setStartTime(value)

    var endTime: LocalTime?
        get() = currentState.endTime
        set(value) = timeService.setEndTime(value)
}