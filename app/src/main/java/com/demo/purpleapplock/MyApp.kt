package com.demo.purpleapplock

import android.app.Application
import com.demo.purpleapplock.util.AcRegister
import com.demo.purpleapplock.util.AppManager
import com.tencent.mmkv.MMKV

lateinit var application: Application
class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        application=this
        MMKV.initialize(this)
        AcRegister.register(this)
        AppManager.getInstalledAppList(this)
    }
}