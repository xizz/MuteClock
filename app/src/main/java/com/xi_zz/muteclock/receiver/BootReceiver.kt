package com.xi_zz.muteclock.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

// adb root
// adb shell am broadcast -a android.intent.action.BOOT_COMPLETED
// adb shell am broadcast -a android.intent.action.LOCKED_BOOT_COMPLETED
// The above doesn't work. Just do the following
// adb reboot

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_LOCKED_BOOT_COMPLETED.equals(intent.action)) {
            Log.i("xizz", "Received action: " + intent.action)
        }
    }
}
