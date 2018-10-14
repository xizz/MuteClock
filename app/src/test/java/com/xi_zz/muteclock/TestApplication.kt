package com.xi_zz.muteclock

import com.xi_zz.muteclock.injection.DaggerTestAppComponent
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication


class TestApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestAppComponent.builder().application(this).build()
    }
}