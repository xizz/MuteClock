package com.xi_zz.muteclock

import java.time.LocalTime
import java.util.Calendar


val LocalTime.calendar: Calendar
    get() = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
    }
