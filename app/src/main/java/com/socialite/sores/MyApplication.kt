package com.socialite.sores

import android.app.Application
import com.microsoft.appcenter.AppCenter
import com.microsoft.appcenter.analytics.Analytics
import com.microsoft.appcenter.crashes.Crashes
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        AppCenter.start(
            this, "2891441a-5325-4e83-9ea5-434bc56afbb7",
            Analytics::class.java, Crashes::class.java
        )
    }
}
