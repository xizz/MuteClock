package com.xi_zz.muteclock.injection

import android.app.AlarmManager
import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.media.AudioManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal class AppModule {
    @Provides
    @Singleton
    internal fun provideAlarmManager(application: Application): AlarmManager =
        application.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @Provides
    @Singleton
    internal fun provideAudioManager(application: Application): AudioManager =
        application.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    @Provides
    @Singleton
    internal fun provideNotificationManager(application: Application): NotificationManager =
        application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}