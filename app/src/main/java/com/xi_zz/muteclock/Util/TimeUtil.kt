package com.xi_zz.muteclock.Util

import java.time.LocalTime
import java.util.Calendar


val LocalTime.calendar: Calendar
    get() = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.MILLISECOND, nano * 1000)
    }

val Long.calendar: Calendar get() = Calendar.getInstance().apply { timeInMillis = this@calendar }

val Calendar.localTime: LocalTime get() = LocalTime.of(get(Calendar.HOUR_OF_DAY), get(Calendar.MINUTE))
