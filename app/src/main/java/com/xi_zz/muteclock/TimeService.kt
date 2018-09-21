package com.xi_zz.muteclock

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
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

    companion object {
        const val MUTE = 11
        const val UNMUTE = 22
    }
}

class TimeServiceImp @Inject constructor(ctx: Context) : TimeService {
    private val appContext = ctx.applicationContext
    private val preferences = appContext.getSharedPreferences(PREF_TIME, Context.MODE_PRIVATE)
    private val alarmManager = appContext.getSystemService(ALARM_SERVICE) as AlarmManager
    private var startTimeSubject = BehaviorSubject.create<Optional<LocalTime>>()
    private var endTimeSubject = BehaviorSubject.create<Optional<LocalTime>>()

    init {
        val startNano = preferences.getLong(KEY_START_TIME, NULL_TIME)
        val endNano = preferences.getLong(KEY_END_TIME, NULL_TIME)
        if (startNano != NULL_TIME)
            startTimeSubject.onNext(Optional.of(LocalTime.ofNanoOfDay(startNano)))
        if (endNano != NULL_TIME)
            endTimeSubject.onNext(Optional.of(LocalTime.ofNanoOfDay(endNano)))
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
        // Save to Disk
        preferences.edit().putLong(key, time.toNanoOfDay()).apply()

        // Notify UI
        subject.onNext(Optional.of(time))

        // Set the Alarm
        val pendingIntent = Intent(appContext, RingerReceiver::class.java).let {
            it.putExtra(EXTRA_MUTE, mute)
            PendingIntent.getBroadcast(appContext, requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        alarmManager.setRepeating(AlarmManager.RTC, time.calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pendingIntent)
    }

    private fun cancelTime(key: String, subject: Subject<Optional<LocalTime>>, mute: Boolean, requestCode: Int) {
        // Remove from Disk
        preferences.edit().remove(key).apply()

        // Notify UI
        subject.onNext(Optional.empty())

        // Remove from Alarm
        val pendingIntent = Intent(appContext, RingerReceiver::class.java).let {
            it.putExtra(EXTRA_MUTE, mute)
            PendingIntent.getBroadcast(appContext, requestCode, it, PendingIntent.FLAG_UPDATE_CURRENT)
        }
        alarmManager.cancel(pendingIntent)
    }
}
