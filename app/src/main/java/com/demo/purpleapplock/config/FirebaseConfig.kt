package com.demo.purpleapplock.config

import com.demo.purpleapplock.ad.AdNumManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.tencent.mmkv.MMKV
import org.json.JSONObject

object FirebaseConfig {

    fun readFireConfig(){
//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                readAdConfig(remoteConfig.getString("purple_ad"))
//            }
//        }
    }

    private fun readAdConfig(string: String){
        try {
            val jsonObject = JSONObject(string)
            AdNumManager.setMax(jsonObject.optInt("max_click"),jsonObject.optInt("max_show"))
            MMKV.defaultMMKV().encode("purple_ad",string)
        }catch (e:Exception){

        }
    }

    fun getAdConfig():String{
        val purpleAd=MMKV.defaultMMKV().decodeString("purple_ad")?:""
        return if (purpleAd.isEmpty()) LocalConfig.localAd else purpleAd
    }
}