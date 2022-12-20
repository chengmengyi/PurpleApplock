package com.demo.purpleapplock.util

import android.os.Bundle
import android.util.Log
import com.demo.purpleapplock.config.FirebaseConfig
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

object SetPointManager {
//    private val remoteConfig= Firebase.analytics

    fun point(name:String){
        printLog("==setpoint==$name")
//        remoteConfig.logEvent(name, Bundle())
    }

    fun setUserProperty(){
        printLog("==setpoint==${FirebaseConfig.paLog.toLowerCase()}方案")
//        remoteConfig.setUserProperty("oa_user_type",FireManager.oaProgram.toLowerCase())
    }
}