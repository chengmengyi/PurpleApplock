package com.demo.purpleapplock.ad

import android.graphics.Outline
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.utils.widget.ImageFilterView
import com.blankj.utilcode.util.SizeUtils
import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.util.AcRegister
import com.demo.purpleapplock.util.RefreshManager
import com.demo.purpleapplock.util.show
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import kotlinx.coroutines.*

class ShowHome(
    private val type:String,
    private val basePage: BasePage,
) {
    private var lastAd:NativeAd?=null
    private var job:Job?=null
    
    fun show(){
        AdManager.load(type)
        stopShow()
        job= GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if (!basePage.resume){
                return@launch
            }
            while (true) {
                if (!isActive) {
                    break
                }

                val ad = AdManager.getAd(type)
                if(basePage.resume && null!=ad && ad is NativeAd){
                    cancel()
                    lastAd?.destroy()
                    lastAd=ad
                    showAd(ad)
                }

                delay(1000L)
            }
        }
    }
    
    private fun showAd(ad: NativeAd) {
        val viewNative = basePage.findViewById<NativeAdView>(R.id.view_native)
        viewNative.iconView=basePage.findViewById(R.id.view_logo)
        (viewNative.iconView as ImageFilterView).setImageDrawable(ad.icon?.drawable)

        viewNative.callToActionView=basePage.findViewById(R.id.view_btn)
        (viewNative.callToActionView as AppCompatTextView).text=ad.callToAction

        if(showCover()){
            viewNative.mediaView=basePage.findViewById(R.id.view_media)
            ad.mediaContent?.let {
                viewNative.mediaView?.apply {
                    setMediaContent(it)
                    setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                    outlineProvider = object : ViewOutlineProvider() {
                        override fun getOutline(view: View?, outline: Outline?) {
                            if (view == null || outline == null) return
                            outline.setRoundRect(
                                0,
                                0,
                                view.width,
                                view.height,
                                SizeUtils.dp2px(8F).toFloat()
                            )
                            view.clipToOutline = true
                        }
                    }
                }
            }
        }

        viewNative.bodyView=basePage.findViewById(R.id.view_desc)
        (viewNative.bodyView as AppCompatTextView).text=ad.body

        viewNative.headlineView=basePage.findViewById(R.id.view_title)
        (viewNative.headlineView as AppCompatTextView).text=ad.headline

        viewNative.setNativeAd(ad)
        basePage.findViewById<AppCompatImageView>(R.id.view_cover).show(false)
        AdNumManager.addShow()
        AdManager.remove(type)
        AdManager.load(type)
        RefreshManager.setBool(type,false)
    }

    private fun showCover()=type==AdManager.HOME || type==AdManager.VPN_HOME || type == AdManager.VPN_RESULT

    fun stopShow(){
        job?.cancel()
        job=null
    }
}