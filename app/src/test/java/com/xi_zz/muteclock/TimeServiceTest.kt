package com.xi_zz.muteclock

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowAlarmManager
import java.time.LocalTime

@Config(sdk = [Build.VERSION_CODES.O])
@RunWith(RobolectricTestRunner::class)
class TimeServiceTest {

    private val application = RuntimeEnvironment.application
    private lateinit var timerService: TimeService
    private lateinit var shadowAlarmManager: ShadowAlarmManager

    @Before
    fun setUp() {
        timerService = TimeServiceImp(application)
        shadowAlarmManager = shadowOf(application.getSystemService(Context.ALARM_SERVICE) as AlarmManager)

    }

    @Test
    fun testSetStartTime() {
        val startTime = LocalTime.of(5, 30)
        timerService.setStartTime(startTime)

        Assert.assertEquals(startTime, shadowAlarmManager.nextScheduledAlarm.triggerAtTime.calendar.localTime)
        timerService.observeStartTime().test()
                .assertNoErrors()
                .assertValue(startTime)
    }

    @Test
    fun testSetEndTime() {
        val endTime = LocalTime.of(15, 45)
        timerService.setEndTime(endTime)

        Assert.assertEquals(endTime, shadowAlarmManager.nextScheduledAlarm.triggerAtTime.calendar.localTime)
        timerService.observeEndTime().test()
                .assertNoErrors()
                .assertValue(endTime)
    }
}