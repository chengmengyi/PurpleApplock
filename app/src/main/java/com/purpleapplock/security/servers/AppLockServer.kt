package com.purpleapplock.security.servers

import android.app.*
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.text.TextUtils
import com.purpleapplock.security.overlay.AppLockOverlay
import com.purpleapplock.security.util.AppManager
import kotlinx.coroutines.*


class AppLockServer: Service() {
    private var showPackageName=""
    private val notificationId="PurpleId"
    private val notificationName="PurpleAppLock"

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        AppLockOverlay.initOverlay(this)
        showNotification()
        check()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }

    private fun check(){
        GlobalScope.launch{
            while (true){
                delay(200)
                val topAppName = getTopAppName(this@AppLockServer)
                withContext(Dispatchers.Main){
                    if (!topAppName.isNullOrEmpty()){
                        if (topAppName!=showPackageName){
                            if (AppManager.checkAppLocked(topAppName)&& Settings.canDrawOverlays(this@AppLockServer)){
                                AppLockOverlay.show()
                            }else{
                                AppLockOverlay.hide()
                            }
                        }
                        showPackageName=topAppName
                    }
                }
            }
        }
    }

    private fun getTopAppName(context: Context): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            val activityManager=context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val appTasks = activityManager.getRunningTasks(1)
            if (null != appTasks && !appTasks.isEmpty()) {
                return appTasks[0].topActivity!!.packageName
            }
        } else {
            //5.0以后需要用这方法
            val sUsageStatsManager =
                context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
            val endTime = System.currentTimeMillis()
            val beginTime = endTime - 10000
            var result = ""
            val event = UsageEvents.Event()
            val usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime)
            while (usageEvents.hasNextEvent()) {
                usageEvents.getNextEvent(event)
                if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                    result = event.packageName
                }
            }
            if (!TextUtils.isEmpty(result)) {
                return result
            }
        }
        return ""
    }

    private fun showNotification(){
//        val builder = NotificationCompat.Builder(this, notificationId)
//            .setWhen(System.currentTimeMillis())
//            .setContentTitle(getString(R.string.app_name))
//            .setCategory(NotificationCompat.CATEGORY_SERVICE)
//            .setPriority(NotificationCompat.PRIORITY_MAX)
//            .setChannelId(notificationId)
//
//        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(notificationId, "PurpleAppLock", IMPORTANCE_HIGH)
//            channel.enableLights(true)
//            channel.setShowBadge(true)
//            manager.createNotificationChannel(channel)
//            builder.setContentTitle(getString(R.string.app_name))
//            builder.setContentText("App lock is running")
//            builder.setSmallIcon(R.mipmap.ic_launcher_round)
//            builder.setOngoing(true)
//            builder.setOnlyAlertOnce(true)
//            startForeground(1, builder.build())
//        }
    }
}