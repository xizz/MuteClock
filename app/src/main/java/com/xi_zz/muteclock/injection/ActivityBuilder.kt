package com.xi_zz.muteclock.injection

import com.xi_zz.muteclock.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {
    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}