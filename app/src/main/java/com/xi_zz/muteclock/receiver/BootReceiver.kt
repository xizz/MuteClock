package com.xi_zz.muteclock.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.xi_zz.muteclock.Util.APP_TAG
import com.xi_zz.muteclock.service.TimeService
import dagger.android.AndroidInjection
import javax.inject.Inject

// adb root
// adb shell am broadcast -n com.xi_zz.muteclock/com.xi_zz.muteclock.receiver.BootReceiver -a android.intent.action.BOOT_COMPLETED
// adb shell am broadcast -n com.xi_zz.muteclock/com.xi_zz.muteclock.receiver.BootReceiver -a android.intent.action.LOCKED_BOOT_COMPLETED
// The above doesn't work. Just do the following
// adb reboot

class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var timeService: TimeService

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)

        if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.action)) {
            Log.i(APP_TAG, "Received action: " + intent.action)
            timeService.resetAlarm()
        }
    }
}
