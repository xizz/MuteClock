package com.xi_zz.muteclock

import com.xi_zz.muteclock.injection.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication

class MainApplication : DaggerApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}