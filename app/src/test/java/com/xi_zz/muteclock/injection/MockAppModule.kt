package com.xi_zz.muteclock.injection

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.SharedPreferences
import android.media.AudioManager
import com.xi_zz.muteclock.service.TimeService
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

@Module
internal class MockAppModule {

    // Android Managers

    @Provides
    internal fun provideTimePreferences(): SharedPreferences {
        val preferences: SharedPreferences = mockk(relaxed = true)
        val slot = slot<Long>()
        every { preferences.getLong(any(), capture(slot)) } answers { slot.captured }
        return preferences
    }

    @Provides
    internal fun provideAlarmManager(): AlarmManager =
        mockk(relaxed = true)

    @Provides
    internal fun provideAudioManager(): AudioManager =
        mockk(relaxed = true)

    @Provides
    internal fun provideNotificationManager(): NotificationManager {
        val notificationManager: NotificationManager = mockk(relaxed = true)
        every { notificationManager.isNotificationPolicyAccessGranted } returns true
        return notificationManager
    }

    // Services
    @Provides
    internal fun provideTimeService(): TimeService =
        mockk(relaxed = true)

}