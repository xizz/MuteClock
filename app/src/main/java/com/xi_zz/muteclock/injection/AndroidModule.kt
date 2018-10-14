package com.xi_zz.muteclock.injection

import com.xi_zz.muteclock.receiver.AlarmReceiver
import com.xi_zz.muteclock.receiver.BootReceiver
import com.xi_zz.muteclock.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal abstract class AndroidModule {
    @ContributesAndroidInjector
    internal abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun bindBootReceiver(): BootReceiver

    @ContributesAndroidInjector
    internal abstract fun bindAlarmReceiveReceiver(): AlarmReceiver
}