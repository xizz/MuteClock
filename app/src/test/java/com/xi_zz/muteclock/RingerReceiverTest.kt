package com.xi_zz.muteclock

import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import com.xi_zz.muteclock.Util.EXTRA_MUTE
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O])
@RunWith(RobolectricTestRunner::class)
class RingerReceiverTest {

    private val application = RuntimeEnvironment.application
    private lateinit var ringerReceiver: RingerReceiver
    private lateinit var audioManager: AudioManager

    @Before
    fun setUp() {
        ringerReceiver = RingerReceiver()
        audioManager = application.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    }

    @Test
    fun testMuteAudioManager() {
        audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
        val intent = Intent(application, RingerReceiver::class.java).putExtra(EXTRA_MUTE, true)
        ringerReceiver.onReceive(application, intent)
        Assert.assertEquals(AudioManager.RINGER_MODE_SILENT, audioManager.ringerMode)
    }

    @Test
    fun testUnmuteAudioManager() {
        audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        val intent = Intent(application, RingerReceiver::class.java).putExtra(EXTRA_MUTE, false)
        ringerReceiver.onReceive(application, intent)
        Assert.assertEquals(AudioManager.RINGER_MODE_NORMAL, audioManager.ringerMode)
    }
}