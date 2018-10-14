package com.xi_zz.muteclock

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import com.xi_zz.muteclock.Util.KEY_END_TIME
import com.xi_zz.muteclock.Util.KEY_START_TIME
import com.xi_zz.muteclock.Util.NULL_TIME
import com.xi_zz.muteclock.Util.calendar
import com.xi_zz.muteclock.Util.checkAndAskForNotificationPolicyAccess
import com.xi_zz.muteclock.receiver.AlarmReceiver
import com.xi_zz.muteclock.service.TimeService
import com.xi_zz.muteclock.service.TimeServiceImp
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.time.LocalTime
import java.util.Optional

@Config(sdk = [Build.VERSION_CODES.O])
@RunWith(RobolectricTestRunner::class)
class TimeServiceImpTest {

    private lateinit var timerService: TimeServiceImp

    private val application = RuntimeEnvironment.application

    @MockK
    private lateinit var preferences: SharedPreferences

    @MockK
    private lateinit var alarmManager: AlarmManager

    @MockK
    private lateinit var notificationManager: NotificationManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)
        val slot = slot<Long>()
        every { preferences.getLong(any(), capture(slot)) } answers { slot.captured }
        every { notificationManager.isNotificationPolicyAccessGranted } returns true
        timerService = TimeServiceImp(application, preferences, alarmManager, notificationManager)
    }

    @Test
    fun testSetStartTime() {
        val time = LocalTime.of(5, 30)
        timerService.setStartTime(time)

        verify { notificationManager.checkAndAskForNotificationPolicyAccess(application) }

        verify { preferences.edit().putLong(KEY_START_TIME, time.toNanoOfDay()).apply() }

        val pendingIntent = Intent(application, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(application, TimeService.MUTE, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        verify { alarmManager.setRepeating(AlarmManager.RTC, time.calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent) }

        timerService.observeStartTime().test()
            .assertNoErrors()
            .assertValue(Optional.of(time))
    }

    @Test
    fun testSetEndTime() {
        val time = LocalTime.of(15, 45)
        timerService.setEndTime(time)

        verify { notificationManager.checkAndAskForNotificationPolicyAccess(application) }

        verify { preferences.edit().putLong(KEY_END_TIME, time.toNanoOfDay()).apply() }

        val pendingIntent = Intent(application, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(application, TimeService.UNMUTE, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        verify { alarmManager.setRepeating(AlarmManager.RTC, time.calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent) }

        timerService.observeEndTime().test()
            .assertNoErrors()
            .assertValue(Optional.of(time))
    }

    @Test
    fun testNoObservableEventIfTimeNotSaved() {
        timerService.resetAlarm()
        timerService.observeStartTime().test().assertValue(Optional.empty())
        timerService.observeEndTime().test().assertValue(Optional.empty())
    }

    @Test
    fun testHasObservableEventIfTimeSaved() {
        val startNano = 5000L
        val endNano = 15000L

        every { preferences.getLong(KEY_START_TIME, NULL_TIME) } returns startNano
        every { preferences.getLong(KEY_END_TIME, NULL_TIME) } returns endNano

        timerService.resetAlarm()

        timerService.observeStartTime().test().assertValue(Optional.of(LocalTime.ofNanoOfDay(startNano)))
        timerService.observeEndTime().test().assertValue(Optional.of(LocalTime.ofNanoOfDay(endNano)))
    }

    @Test
    fun testCancelStartTime() {
        verify { preferences.edit().remove(KEY_START_TIME).apply() }

        val pendingIntent = Intent(application, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(application, TimeService.MUTE, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        verify { alarmManager.cancel(pendingIntent) }

        timerService.observeStartTime().test().assertValue(Optional.empty())
    }

    @Test
    fun testCancelEndTime() {
        timerService.setEndTime(null)

        verify { preferences.edit().remove(KEY_END_TIME).apply() }

        val pendingIntent = Intent(application, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(application, TimeService.UNMUTE, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        verify { alarmManager.cancel(pendingIntent) }

        timerService.observeEndTime().test().assertValue(Optional.empty())
    }

}