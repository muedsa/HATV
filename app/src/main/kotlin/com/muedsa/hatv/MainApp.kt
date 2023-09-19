package com.muedsa.hatv

import EnvConfig
import android.app.Application
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import timber.log.Timber

@HiltAndroidApp
class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {
            FirebaseCrashlytics.getInstance().recordException(it)
        }
        if (EnvConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}