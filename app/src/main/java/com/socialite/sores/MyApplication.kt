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
            this, "ed558f17-9484-4ff1-bede-5a7eb48efe95",
            Analytics::class.java, Crashes::class.java
        )
    }
}
