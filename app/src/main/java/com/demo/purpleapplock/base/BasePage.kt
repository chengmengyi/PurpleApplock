package com.demo.purpleapplock.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.demo.purpleapplock.util.height
import com.gyf.immersionbar.ImmersionBar

abstract class BasePage(private val layoutId:Int):AppCompatActivity() {
    var resume=false
    protected lateinit var immersionBar: ImmersionBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        height()
        setContentView(layoutId)
        immersionBar=ImmersionBar.with(this).apply {
            statusBarAlpha(0f)
            autoDarkModeEnable(true)
            statusBarDarkFont(true)
            init()
        }
        initView()
    }

    abstract fun initView()

    override fun onResume() {
        super.onResume()
        resume=true
    }

    override fun onPause() {
        super.onPause()
        resume=false
    }

    override fun onStop() {
        super.onStop()
        resume=false
    }
}