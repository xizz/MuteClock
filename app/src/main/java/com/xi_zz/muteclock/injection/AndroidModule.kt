package com.xi_zz.muteclock.injection

import com.xi_zz.muteclock.receiver.BootReceiver
import com.xi_zz.muteclock.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidModule {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindBootReceiver(): BootReceiver
}