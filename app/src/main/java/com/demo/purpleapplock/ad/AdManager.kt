package com.demo.purpleapplock.ad

import com.demo.purpleapplock.application
import com.demo.purpleapplock.bean.AdBean
import com.demo.purpleapplock.bean.LocalBean
import com.demo.purpleapplock.config.FirebaseConfig
import com.demo.purpleapplock.util.printLog
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAdOptions
import org.json.JSONObject

object AdManager {
    const val OPEN="purple_open"
    const val HOME="purple_home"
    const val LOCK_HOME="purple_lock_home"
    const val LOCK="purple_lock"
    const val VPN_HOME="pa_vpnhome_n"
    const val VPN_RESULT="pa_vpnresult_n"
    const val VPN_CONNECT="pa_vpnlink_i"
    const val VPN_BACK="pa_server_i"
    const val HOME_CLICK="pa_homeclick_i"

    var showingOpen=false
    private val loading= arrayListOf<String>()
    private val adMap= hashMapOf<String,AdBean>()

    fun load(type:String,open: Boolean=true){
        if(AdNumManager.limit()){
            printLog("limit")
            return
        }

        if (loading.contains(type)){
            printLog("$type loading")
            return
        }
        if(adMap.containsKey(type)){
            val adBean = adMap[type]
            if (null!=adBean?.ad){
                if(adBean.expired()){
                    remove(type)
                }else{
                    printLog("$type cache")
                    return
                }
            }
        }
        val list = getList(type)
        if (list.isNotEmpty()){
            loading.add(type)
            loop(type,list.iterator(),open)
        }
    }

    private fun loop(type: String, iterator: Iterator<LocalBean>, open: Boolean){
        startLoad(type,iterator.next()){
            if (null==it){
                if (iterator.hasNext()){
                    loop(type,iterator,open)
                }else{
                    loading.remove(type)
                    if (type== OPEN&&open){
                        load(type,open = false)
                    }
                }
            }else{
                loading.remove(type)
                adMap[type]=it
            }
        }
    }

    private fun startLoad(type:String,localBean: LocalBean,result:(admob:AdBean?)->Unit){
        printLog("load $type,--->${localBean.purple_id},${localBean.purple_sort},${localBean.purple_type}")
        when(localBean.purple_type){
            "o"->{
                loadOpen(type,localBean,result)
            }
            "c"->{
                loadInterstitial(type,localBean,result)
            }
            "y"->{
                loadNative(type,localBean,result)
            }
        }
    }

    private fun loadOpen(type:String,localBean: LocalBean,result:(admob:AdBean?)->Unit){
        AppOpenAd.load(
            application,
            localBean.purple_id,
            AdRequest.Builder().build(),
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
            object : AppOpenAd.AppOpenAdLoadCallback(){
                override fun onAdLoaded(p0: AppOpenAd) {
                    printLog("load $type ad success")
                    result.invoke(AdBean(p0,System.currentTimeMillis()))
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    printLog("load $type fail,${p0.message}")
                    result.invoke(null)
                }
            }
        )
    }

    private fun loadInterstitial(type:String,localBean: LocalBean,result:(admob:AdBean?)->Unit){
        InterstitialAd.load(
            application,
            localBean.purple_id,
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback(){
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    printLog("load $type fail,${p0.message}")
                    result.invoke(null)
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    printLog("load $type ad success")
                    result.invoke(AdBean(p0,System.currentTimeMillis()))
                }
            }
        )
    }

    private fun loadNative(type:String,localBean: LocalBean,result:(admob:AdBean?)->Unit){
        AdLoader.Builder(
            application,
            localBean.purple_id,
        ).forNativeAd {
            printLog("load $type ad success")
            result.invoke(AdBean(it,System.currentTimeMillis()))
        }
            .withAdListener(object : AdListener(){
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    printLog("load $type fail,${p0.message}")
                    result.invoke(null)
                }

                override fun onAdClicked() {
                    super.onAdClicked()
                    AdNumManager.addClick()
                }
            })
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(
                        NativeAdOptions.ADCHOICES_BOTTOM_LEFT
                    )
                    .build()
            )
            .build()
            .loadAd(AdRequest.Builder().build())
    }

    private fun getList(type: String):List<LocalBean>{
        val list= arrayListOf<LocalBean>()
        try {
            val jsonArray = JSONObject(FirebaseConfig.getAdConfig()).getJSONArray(type)
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                list.add(
                    LocalBean(
                        jsonObject.optString("purple_source"),
                        jsonObject.optString("purple_id"),
                        jsonObject.optString("purple_type"),
                        jsonObject.optInt("purple_sort"),
                    )
                )
            }
        }catch (e:Exception){
            printLog("====${e.message}")
        }
        return list.filter { it.purple_source == "admob" }.sortedByDescending { it.purple_sort }
    }

    fun remove(type: String){
        adMap.remove(type)
    }

    fun getAd(type: String)= adMap[type]?.ad

    fun removeAll(){
        adMap.clear()
        loading.clear()
        load(OPEN)
        load(HOME)
        load(LOCK_HOME)
        load(LOCK)
        load(VPN_HOME)
        load(VPN_RESULT)
        load(VPN_CONNECT)
        load(HOME_CLICK)
    }
}
