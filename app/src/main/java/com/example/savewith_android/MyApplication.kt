package com.example.savewith_android

import android.app.Application
import android.content.Context

class MyApplication : Application() {
//    companion object {
//        lateinit var prefs: PreferenceUtil
//    }

//    override fun onCreate() {
//        prefs = PreferenceUtil(applicationContext)
//        super.onCreate()
//    }
companion object {
    lateinit var appContext: Context
        private set
}

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}