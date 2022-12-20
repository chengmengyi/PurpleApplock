package com.demo.purpleapplock

import android.app.ActivityManager
import android.app.Application
import com.demo.purpleapplock.config.FirebaseConfig
import com.demo.purpleapplock.page.HomePage
import com.demo.purpleapplock.util.AcRegister
import com.demo.purpleapplock.util.AppManager
import com.github.shadowsocks.Core
import com.tencent.mmkv.MMKV

lateinit var application: Application
class MyApp:Application() {
    override fun onCreate() {
        super.onCreate()
        application=this
        MMKV.initialize(this)
        Core.init(this,HomePage::class)
        if (!packageName.equals(processName(this))){
            return
        }
        AcRegister.register(this)
        AppManager.getInstalledAppList(this)
        FirebaseConfig.readFireConfig()
        FirebaseConfig.checkIR()
    }

    private fun processName(applicationContext: Application): String {
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