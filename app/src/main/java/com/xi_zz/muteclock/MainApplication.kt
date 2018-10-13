package com.xi_zz.muteclock

import android.app.Activity
import android.app.NotificationManager
import android.os.Bundle
import com.xi_zz.muteclock.Util.checkAndAskForNotificationPolicyAccess
import com.xi_zz.muteclock.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MainApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {}

            override fun onActivityResumed(activity: Activity?) {}

            override fun onActivityStarted(activity: Activity?) {}

            override fun onActivityDestroyed(activity: Activity?) {}

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}

            override fun onActivityStopped(activity: Activity?) {}

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
                (getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager)
                    .checkAndAskForNotificationPolicyAccess(this@MainApplication)
            }

        })
    }
}