package com.xi_zz.muteclock.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.xi_zz.muteclock.R
import com.xi_zz.muteclock.TimeService
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var timeService: TimeService

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
