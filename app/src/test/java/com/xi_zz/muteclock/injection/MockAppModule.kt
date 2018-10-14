package com.xi_zz.muteclock.injection

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.SharedPreferences
import android.media.AudioManager
import dagger.Module
import dagger.Provides
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

@Module
internal class MockAppModule {
    @Provides
    internal fun provideTimePreferences(): SharedPreferences {
        val preferences: SharedPreferences = mockk()
        val slot = slot<Long>()
        every { preferences.getLong(any(), capture(slot)) } answers { slot.captured }
        return preferences
    }

    @Provides
    internal fun provideAlarmManager(): AlarmManager =
        mockk()

    @Provides
    internal fun provideAudioManager(): AudioManager =
        mockk()

    @Provides
    internal fun provideNotificationManager(): NotificationManager {
        val notificationManager: NotificationManager = mockk()
        every { notificationManager.isNotificationPolicyAccessGranted } returns true
        return notificationManager
    }


}