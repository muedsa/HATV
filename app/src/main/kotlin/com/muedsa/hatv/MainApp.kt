package com.muedsa.hatv

import EnvConfig
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (EnvConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}