package com.xi_zz.muteclock

import android.app.AlarmManager
import android.content.Context
import android.content.SharedPreferences
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
class TimeServiceImpTest {

    private lateinit var timerService: TimeService

    private val application = RuntimeEnvironment.application
    private var preferences: SharedPreferences = application.getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE)
    private var shadowAlarmManager: ShadowAlarmManager = shadowOf(application.getSystemService(Context.ALARM_SERVICE) as AlarmManager)


    @Before
    fun setUp() {
        timerService = TimeServiceImp(application)
    }

    @Test
    fun testSetStartTime() {
        val startTime = LocalTime.of(5, 30)
        timerService.setStartTime(startTime)

        Assert.assertEquals(startTime, shadowAlarmManager.nextScheduledAlarm.triggerAtTime.calendar.localTime)
        Assert.assertEquals(startTime.toNanoOfDay(), preferences.getLong(KEY_START_TIME, 0))
        timerService.observeStartTime().test()
                .assertNoErrors()
                .assertValue(startTime)
    }

    @Test
    fun testSetEndTime() {
        val endTime = LocalTime.of(15, 45)
        timerService.setEndTime(endTime)

        Assert.assertEquals(endTime, shadowAlarmManager.nextScheduledAlarm.triggerAtTime.calendar.localTime)
        Assert.assertEquals(endTime.toNanoOfDay(), preferences.getLong(KEY_END_TIME, 0))
        timerService.observeEndTime().test()
                .assertNoErrors()
                .assertValue(endTime)
    }

    @Test
    fun testNoObservableEventIfTimeNotSaved() {
        timerService = TimeServiceImp(application)

        timerService.observeStartTime().test().assertEmpty()
        timerService.observeEndTime().test().assertEmpty()
    }

    @Test
    fun testHasObservableEventIfTimeSaved() {
        val startNano = 5000L
        val endNano = 15000L
        preferences.edit().putLong(KEY_START_TIME, startNano).commit()
        preferences.edit().putLong(KEY_END_TIME, endNano).commit()
        timerService = TimeServiceImp(application)

        timerService.observeStartTime().test().assertValue(LocalTime.ofNanoOfDay(startNano))
        timerService.observeEndTime().test().assertValue(LocalTime.ofNanoOfDay(endNano))
    }

}