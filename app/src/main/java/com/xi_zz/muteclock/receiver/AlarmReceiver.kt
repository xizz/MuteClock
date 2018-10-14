package com.xi_zz.muteclock.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.util.Log
import android.widget.Toast
import com.xi_zz.muteclock.Util.APP_TAG
import com.xi_zz.muteclock.Util.EXTRA_MUTE
import com.xi_zz.muteclock.Util.checkAndAskForNotificationPolicyAccess
import dagger.android.AndroidInjection
import javax.inject.Inject

// adb shell am broadcast -n com.xi_zz.muteclock/com.xi_zz.muteclock.receiver.AlarmReceiver --es extra_test "adb_testing"
// adb shell am broadcast -n com.xi_zz.muteclock/com.xi_zz.muteclock.receiver.AlarmReceiver -f 32 --ez EXTRA_MUTE true
// -f 32 or Intent.FLAG_INCLUDE_STOPPED_PACKAGES has no effect.
// It works anyway for most devices and doesn't work regardless for onePlus

// adb shell am broadcast -n com.xi_zz.muteclock/com.xi_zz.muteclock.receiver.AlarmReceiver --ez EXTRA_MUTE true

class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationManager: NotificationManager
    @Inject
    lateinit var audioManager: AudioManager

    override fun onReceive(context: Context, intent: Intent) {
        AndroidInjection.inject(this, context)

        val mute = intent.getBooleanExtra(EXTRA_MUTE, false)

        if (notificationManager.checkAndAskForNotificationPolicyAccess(context)) {
            Toast.makeText(context, "Phone Muted: $mute", Toast.LENGTH_LONG).show()
            Log.i(APP_TAG, "Phone Muted: $mute")
            audioManager.ringerMode = if (mute) AudioManager.RINGER_MODE_SILENT else AudioManager.RINGER_MODE_NORMAL
        }
    }
}