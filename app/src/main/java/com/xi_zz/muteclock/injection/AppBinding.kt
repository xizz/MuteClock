package com.xi_zz.muteclock.injection

import com.xi_zz.muteclock.TimeService
import com.xi_zz.muteclock.TimeServiceImp
import dagger.Binds
import dagger.Module

@Module
internal abstract class AppBinding {
    @Binds
    internal abstract fun provideTimeService(service: TimeServiceImp): TimeService
}