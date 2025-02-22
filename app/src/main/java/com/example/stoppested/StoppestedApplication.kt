package com.example.stoppested

import android.app.Application
import com.example.stoppested.network.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StoppestedApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@StoppestedApplication)
            modules(appModule, networkModule)
        }
    }
}