package com.demo.purpleapplock.ad

import android.app.Activity
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.util.printLog
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.appopen.AppOpenAd
import com.google.android.gms.ads.interstitial.InterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ShowLock(
    private val type:String,
    private val activity: Activity,
    private val result:(request:Boolean)->Unit
) {

    fun show(){
        val ad = AdManager.getAd(type)
        if(null!=ad&&!AdManager.showingOpen){
            when(ad){
                is InterstitialAd ->{
                    showInterstitial(ad)
                }
                is AppOpenAd ->{
                    showOpen(ad)
                }
            }
        }else{
            result.invoke(true)
        }
    }

    private fun showOpen(ad : AppOpenAd){
        ad.fullScreenContentCallback=callback
        ad.show(activity)
    }

    private fun showInterstitial(ad : InterstitialAd){
        ad.fullScreenContentCallback=callback
        ad.show(activity)
    }

    private val callback=object : FullScreenContentCallback() {
        override fun onAdDismissedFullScreenContent() {
            super.onAdDismissedFullScreenContent()
            AdManager.showingOpen=false
            showFinish()
        }

        override fun onAdShowedFullScreenContent() {
            super.onAdShowedFullScreenContent()
            AdManager.showingOpen=true
            AdNumManager.addShow()
            AdManager.remove(type)
        }

        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
            super.onAdFailedToShowFullScreenContent(p0)
            AdManager.showingOpen=false
            AdManager.remove(type)
            showFinish()
        }

        override fun onAdClicked() {
            super.onAdClicked()
            AdNumManager.addClick()
        }

        private fun showFinish(){
            if (type!=AdManager.OPEN){
                AdManager.load(type)
            }
            GlobalScope.launch(Dispatchers.Main) {
                delay(200L)
                result.invoke(false)
            }
        }
    }
}