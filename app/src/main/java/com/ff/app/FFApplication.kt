package com.ff.app

import android.app.Application
import android.content.Context

class FFApplication : Application() {
    companion object {
        lateinit var appContext: Context
    }
    lateinit var coreManager: com.ff.app.core.CoreManager

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
        coreManager = com.ff.app.core.CoreManager(this)
    }
}
