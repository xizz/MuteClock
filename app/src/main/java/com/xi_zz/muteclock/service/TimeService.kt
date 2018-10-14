package com.xi_zz.muteclock.service

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.xi_zz.muteclock.Util.EXTRA_MUTE
import com.xi_zz.muteclock.Util.KEY_END_TIME
import com.xi_zz.muteclock.Util.KEY_START_TIME
import com.xi_zz.muteclock.Util.NULL_TIME
import com.xi_zz.muteclock.Util.PREF_TIME
import com.xi_zz.muteclock.Util.calendar
import com.xi_zz.muteclock.Util.checkAndAskForNotificationPolicyAccess
import com.xi_zz.muteclock.receiver.AlarmReceiver
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.time.LocalTime
import java.util.Optional
import javax.inject.Inject


interface TimeService {
    fun observeStartTime(): Observable<Optional<LocalTime>>
    fun observeEndTime(): Observable<Optional<LocalTime>>
    fun setStartTime(time: LocalTime?)
    fun setEndTime(time: LocalTime?)
    fun resetAlarm()

    companion object {
        const val MUTE = 11
        const val UNMUTE = 22
    }
}

class TimeServiceImp @Inject constructor(
    private val application: Application,
    private val alarmManager: AlarmManager,
    private val notificationManager: NotificationManager
) : TimeService {
    private val preferences = application.getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE)
    private var startTimeSubject = BehaviorSubject.create<Optional<LocalTime>>()
    private var endTimeSubject = BehaviorSubject.create<Optional<LocalTime>>()

    init {
        resetAlarm()
    }

    override fun observeStartTime(): Observable<Optional<LocalTime>> = startTimeSubject

    override fun observeEndTime(): Observable<Optional<LocalTime>> = endTimeSubject

    override fun setStartTime(time: LocalTime?) {
        if (time != null)
            setTime(KEY_START_TIME, time, startTimeSubject, true, TimeService.MUTE)
        else
            cancelTime(KEY_START_TIME, startTimeSubject, true, TimeService.MUTE)
    }

    override fun setEndTime(time: LocalTime?) {
        if (time != null)
            setTime(KEY_END_TIME, time, endTimeSubject, false, TimeService.UNMUTE)
        else
            cancelTime(KEY_END_TIME, endTimeSubject, false, TimeService.UNMUTE)
    }

    private fun setTime(key: String, time: LocalTime, subject: Subject<Optional<LocalTime>>, mute: Boolean, requestCode: Int) {
        // Permission reminder
        notificationManager.checkAndAskForNotificationPolicyAccess(application)

        // Save to Disk
        preferences.edit().putLong(key, time.toNanoOfDay()).apply()

        // Notify UI
        subject.onNext(Optional.of(time))

        // Set the Alarm
        val pendingIntent = Intent(application, AlarmReceiver::class.java).let {
            it.putExtra(EXTRA_MUTE, mute)
            PendingIntent.getBroadcast(application, requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        alarmManager.setRepeating(AlarmManager.RTC, time.calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
//        alarmManager.setExact(AlarmManager.RTC, time.calendar.timeInMillis, pendingIntent)
    }

    private fun cancelTime(key: String, subject: Subject<Optional<LocalTime>>, mute: Boolean, requestCode: Int) {
        // Remove from Disk
        preferences.edit().remove(key).apply()

        // Notify UI
        subject.onNext(Optional.empty())

        // Remove from Alarm
        val pendingIntent = Intent(application, AlarmReceiver::class.java).let {
            it.putExtra(EXTRA_MUTE, mute)
            PendingIntent.getBroadcast(application, requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmManager.cancel(pendingIntent)
    }

    override fun resetAlarm() {
        val startNano = preferences.getLong(KEY_START_TIME, NULL_TIME)
        val endNano = preferences.getLong(KEY_END_TIME, NULL_TIME)
        if (startNano != NULL_TIME)
            setTime(KEY_START_TIME, LocalTime.ofNanoOfDay(startNano), startTimeSubject, true, TimeService.MUTE)
        else
            cancelTime(KEY_START_TIME, startTimeSubject, true, TimeService.MUTE)
        if (endNano != NULL_TIME)
            setTime(KEY_END_TIME, LocalTime.ofNanoOfDay(endNano), endTimeSubject, false, TimeService.UNMUTE)
        else
            cancelTime(KEY_END_TIME, endTimeSubject, true, TimeService.MUTE)
    }
}
