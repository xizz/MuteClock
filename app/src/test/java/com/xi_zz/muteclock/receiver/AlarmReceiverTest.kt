package com.xi_zz.muteclock.receiver

import android.content.Intent
import android.media.AudioManager
import android.os.Build
import com.xi_zz.muteclock.TestApplication
import com.xi_zz.muteclock.Util.EXTRA_MUTE
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O], application = TestApplication::class)
@RunWith(RobolectricTestRunner::class)
class AlarmReceiverTest {

    private lateinit var alarmReceiver: AlarmReceiver

    private val application = RuntimeEnvironment.application

    @Before
    fun setUp() {
        alarmReceiver = AlarmReceiver()
    }

    @Test
    fun testMuteAudioManager() {
        val intent = Intent(application, AlarmReceiver::class.java).putExtra(EXTRA_MUTE, true)
        alarmReceiver.onReceive(application, intent)

        verify { alarmReceiver.audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT }
    }

    @Test
    fun testUnmuteAudioManager() {
        val intent = Intent(application, AlarmReceiver::class.java).putExtra(EXTRA_MUTE, false)
        alarmReceiver.onReceive(application, intent)

        verify { alarmReceiver.audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL }
    }
}