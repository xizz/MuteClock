package com.xi_zz.muteclock

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.widget.Toast

// adb shell am broadcast -n com.xi_zz.muteclock/.RingerReceiver --es extra_test "adb_testing"
// adb shell am broadcast -n com.xi_zz.muteclock/.RingerReceiver --ez EXTRA_MUTE true
// adb shell am broadcast -n com.xi_zz.muteclock/.RingerReceiver -f 32 --ez EXTRA_MUTE true
// -f 32 or Intent.FLAG_INCLUDE_STOPPED_PACKAGES has no effect.
// It works anyway for most devices and doesn't work regardless for onePlus

class RingerReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val action = intent.getBooleanExtra(EXTRA_MUTE, false)

        Toast.makeText(context, "AlarmReceiver.onReceive: $action", Toast.LENGTH_LONG).show()

        if (action)
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
        else
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
    }
}