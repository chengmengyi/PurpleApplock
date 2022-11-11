package com.demo.purpleapplock.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.demo.purpleapplock.page.HomePage
import com.demo.purpleapplock.page.MainPage
import com.google.android.gms.ads.AdActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object AcRegister {
    var purpleFront=true
    var refreshNativeAd=true
    private var jumpToMain=false
    private var job: Job?=null


    fun register(application: Application){
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            private var pages=0
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                pages++
                job?.cancel()
                job=null
                if (pages==1){
                    start(activity)
                }
            }

            override fun onActivityResumed(activity: Activity) {}

            override fun onActivityPaused(activity: Activity) {}

            override fun onActivityStopped(activity: Activity) {
                pages--
                if (pages<=0){
                    stop()
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

            override fun onActivityDestroyed(activity: Activity) {}
        })
    }

    private fun start(activity: Activity){
        purpleFront=true
        if (jumpToMain){
            if (ActivityUtils.isActivityExistsInStack(HomePage::class.java)){
                activity.startActivity(Intent(activity, MainPage::class.java))
            }
        }
        jumpToMain=false
    }

    private fun stop(){
        purpleFront=false
        refreshNativeAd=true
        job= GlobalScope.launch {
            delay(3000L)
            jumpToMain=true
            ActivityUtils.finishActivity(MainPage::class.java)
            ActivityUtils.finishActivity(AdActivity::class.java)
        }
    }
}