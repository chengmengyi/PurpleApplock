package com.demo.purpleapplock.ad

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

class ShowOpen(
    private val type:String,
    private val basePage: BasePage,
    private val close:()->Unit
) {

    fun show(result:(b:Boolean)->Unit){
        val ad = AdManager.getAd(type)
        if (ad==null&&AdNumManager.limit()){
            result.invoke(true)
        }else{
            if(ad!=null){
                if (AdManager.showingOpen||!basePage.resume){
                    result.invoke(false)
                }else{
                    result.invoke(false)
                    printLog("show $type ad")
                    when(ad){
                        is InterstitialAd ->{
                            showInterstitial(ad)
                        }
                        is AppOpenAd ->{
                            showOpen(ad)
                        }
                    }
                }
            }
        }
    }

    private fun showOpen(ad : AppOpenAd){
        ad.fullScreenContentCallback=callback
        ad.show(basePage)
    }

    private fun showInterstitial(ad : InterstitialAd){
        ad.fullScreenContentCallback=callback
        ad.show(basePage)
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
                if (basePage.resume){
                    close.invoke()
                }
            }
        }
    }
}