package com.demo.purpleapplock.page

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import com.blankj.utilcode.util.ActivityUtils
import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BasePage
import kotlinx.android.synthetic.main.activity_main.*

class MainPage : BasePage(R.layout.activity_main) {
    private var valueAnimator:ValueAnimator?=null
    private var objectAnimator: ObjectAnimator?=null

    override fun initView() {
        animator()
    }

    private fun animator(){
        valueAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration=3000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val progress = it.animatedValue as Int
                tv_progress.text="$progress%"
            }
            doOnEnd { jumpToHome() }
            start()
        }

        objectAnimator=ObjectAnimator.ofFloat(iv_progress, "rotation", 0f, 360f).apply {
            duration=1000L
            repeatCount= ValueAnimator.INFINITE
            repeatMode=ObjectAnimator.RESTART
            start()
        }
    }

    private fun jumpToHome(){
        val activityExistsInStack = ActivityUtils.isActivityExistsInStack(HomePage::class.java)
        if (!activityExistsInStack){
            startActivity(Intent(this, HomePage::class.java))
        }
        finish()
    }

    private fun stopAnimator(){
        valueAnimator?.removeAllUpdateListeners()
        valueAnimator?.cancel()
        valueAnimator=null
    }

    override fun onResume() {
        super.onResume()
        valueAnimator?.resume()
    }

    override fun onPause() {
        super.onPause()
        valueAnimator?.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopAnimator()
        objectAnimator?.cancel()
        objectAnimator=null
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            return true
        }
        return false
    }
}