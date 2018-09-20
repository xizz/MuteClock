package com.xi_zz.muteclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.widget.Toast
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.time.LocalTime
import javax.inject.Inject


interface TimeService {
    fun observeStartTime(): Observable<LocalTime>
    fun setStartTime(time: LocalTime)
    fun observeEndTime(): Observable<LocalTime>
    fun setEndTime(time: LocalTime)

    companion object {
        const val MUTE = 11
        const val UNMUTE = 22
    }
}

class TimeServiceImp @Inject constructor(ctx: Context) : TimeService {
    private val appContext = ctx.applicationContext
    private val preferences = appContext.getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE)
    private val alarmManager = appContext.getSystemService(ALARM_SERVICE) as AlarmManager
    private var startTimeSubject: BehaviorSubject<LocalTime> = BehaviorSubject.create<LocalTime>()
    private var endTimeSubject: BehaviorSubject<LocalTime> = BehaviorSubject.create<LocalTime>()

    init {
        val startNano = preferences.getLong(KEY_START_TIME, NULL_TIME)
        val endNano = preferences.getLong(KEY_END_TIME, NULL_TIME)
        if (startNano != NULL_TIME)
            startTimeSubject.onNext(LocalTime.ofNanoOfDay(startNano))
        if (endNano != NULL_TIME)
            endTimeSubject.onNext(LocalTime.ofNanoOfDay(endNano))
    }
    override fun observeStartTime(): Observable<LocalTime> = startTimeSubject

    override fun observeEndTime(): Observable<LocalTime> = endTimeSubject

    override fun setStartTime(time: LocalTime) {
        // Save to Disk
        appContext.getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE)
                .edit().putLong(KEY_START_TIME, time.toNanoOfDay()).apply()

        // Notify UI
        startTimeSubject.onNext(time)

        // Set the Alarm
        val pendingIntent = Intent(appContext, RingerReceiver::class.java).let {
            it.putExtra(EXTRA_MUTE, true)
            PendingIntent.getBroadcast(appContext, TimeService.MUTE, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        alarmManager.setRepeating(AlarmManager.RTC, time.calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(appContext, "Mute phone at: $time", Toast.LENGTH_SHORT).show()
    }

    override fun setEndTime(time: LocalTime) {
        // Save to Disk
        appContext.getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE)
                .edit().putLong(KEY_END_TIME, time.toNanoOfDay()).apply()

        // Notify UI
        endTimeSubject.onNext(time)

        // Set the Alarm
        val pendingIntent = Intent(appContext, RingerReceiver::class.java).let {
            it.putExtra(EXTRA_MUTE, false)
            PendingIntent.getBroadcast(appContext, TimeService.UNMUTE, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        alarmManager.setRepeating(AlarmManager.RTC, time.calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
        Toast.makeText(appContext, "Unmute phone at: $time", Toast.LENGTH_SHORT).show()
    }
}