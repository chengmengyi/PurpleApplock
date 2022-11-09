package com.demo.purpleapplock

import android.app.Application
import com.demo.purpleapplock.util.AppManager
import com.tencent.mmkv.MMKV

class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        AppManager.getInstalledAppList(this)
    }
}