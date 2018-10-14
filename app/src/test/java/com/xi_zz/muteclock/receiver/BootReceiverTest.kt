package com.xi_zz.muteclock.receiver

import android.content.Intent
import android.os.Build
import com.xi_zz.muteclock.TestApplication
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@Config(sdk = [Build.VERSION_CODES.O], application = TestApplication::class)
@RunWith(RobolectricTestRunner::class)
class BootReceiverTest {

    private lateinit var bootReceiver: BootReceiver

    private val application = RuntimeEnvironment.application

    @Before
    fun setUp() {
        bootReceiver = BootReceiver()
    }

    @Test
    fun testTimeServiceCalled() {
        val intent = Intent().also { it.action = Intent.ACTION_LOCKED_BOOT_COMPLETED }
        bootReceiver.onReceive(application, intent)

        verify { bootReceiver.timeService.resetAlarm() }
    }
}