package com.xi_zz.muteclock

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.Toast
import com.xi_zz.muteclock.Util.EXTRA_MUTE
import com.xi_zz.muteclock.Util.checkAndAskForNotificationPolicyAccess

// adb shell am broadcast -n com.xi_zz.muteclock/.RingerReceiver --es extra_test "adb_testing"
// adb shell am broadcast -n com.xi_zz.muteclock/.RingerReceiver --ez EXTRA_MUTE true
// adb shell am broadcast -n com.xi_zz.muteclock/.RingerReceiver -f 32 --ez EXTRA_MUTE true
// -f 32 or Intent.FLAG_INCLUDE_STOPPED_PACKAGES has no effect.
// It works anyway for most devices and doesn't work regardless for onePlus

class RingerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val mute = intent.getBooleanExtra(EXTRA_MUTE, false)


        if (notificationManager.checkAndAskForNotificationPolicyAccess(context)) {
            Toast.makeText(context, "Phone Muted: $mute", Toast.LENGTH_LONG).show()
            audioManager.ringerMode = if (mute) AudioManager.RINGER_MODE_SILENT else AudioManager.RINGER_MODE_NORMAL
        }
    }
}