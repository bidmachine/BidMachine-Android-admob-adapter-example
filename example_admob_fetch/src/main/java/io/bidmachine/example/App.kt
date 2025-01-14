package io.bidmachine.example

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        AppLogger.initialize(this)
    }
}