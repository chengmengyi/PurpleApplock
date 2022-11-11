package com.purpleapplock.security.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.purpleapplock.security.util.height
import com.gyf.immersionbar.ImmersionBar

abstract class BasePage(private val layoutId:Int):AppCompatActivity() {
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
}