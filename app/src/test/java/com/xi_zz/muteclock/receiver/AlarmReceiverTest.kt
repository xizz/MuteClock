package com.xi_zz.muteclock.receiver

import android.app.NotificationManager
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import com.xi_zz.muteclock.Util.EXTRA_MUTE
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O])
@RunWith(RobolectricTestRunner::class)
class AlarmReceiverTest {

    private val application = RuntimeEnvironment.application
    private lateinit var alarmReceiver: AlarmReceiver

    @MockK
    private lateinit var audioManager: AudioManager

    @MockK
    private lateinit var notificationManager: NotificationManager

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)

        every { notificationManager.isNotificationPolicyAccessGranted } returns true

        alarmReceiver = AlarmReceiver()
        alarmReceiver.audioManager = audioManager
        alarmReceiver.notificationManager = notificationManager
    }

    @Test
    fun testMuteAudioManager() {
        val intent = Intent(application, AlarmReceiver::class.java).putExtra(EXTRA_MUTE, true)
        alarmReceiver.onReceive(application, intent)

        verify { audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT }
    }

    @Test
    fun testUnmuteAudioManager() {
        audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        val intent = Intent(application, AlarmReceiver::class.java).putExtra(EXTRA_MUTE, false)
        alarmReceiver.onReceive(application, intent)
        Assert.assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.ringerMode)
    }
}