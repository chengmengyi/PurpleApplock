package com.purpleapplock.security

import android.app.ActivityManager
import android.app.Application
import com.purpleapplock.security.page.HomePage
import com.purpleapplock.security.util.AppManager
import com.github.shadowsocks.Core
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.tencent.mmkv.MMKV

class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        Core.init(this,HomePage::class)
        MMKV.initialize(this)
        if (!packageName.equals(processName(this))){
            return
        }
        Firebase.initialize(this)
        AppManager.getInstalledAppList(this)
    }

    fun processName(applicationContext: Application): String {
        val pid = android.os.Process.myPid()
        var processName = ""
        val manager = applicationContext.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid === pid) {
                processName = process.processName
            }
        }
        return processName
    }
}