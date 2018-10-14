package com.xi_zz.muteclock.Util

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.xi_zz.muteclock.R

fun NotificationManager.checkAndAskForNotificationPolicyAccess(context: Context): Boolean =
    isNotificationPolicyAccessGranted.also { granted ->
        if (!granted) {
            context.startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
            Toast.makeText(context, R.string.enable_permission, Toast.LENGTH_LONG).show()
        }
    }
