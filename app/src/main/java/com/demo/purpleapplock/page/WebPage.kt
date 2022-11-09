package com.demo.purpleapplock.page

import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.config.LocalConfig
import kotlinx.android.synthetic.main.activity_web.*

class WebPage:BasePage(R.layout.activity_web) {
    override fun initView() {
        immersionBar.statusBarView(view_top).init()
        iv_back.setOnClickListener { finish() }

        webview.apply {
            settings.javaScriptEnabled=true
            loadUrl(LocalConfig.URL)
        }
    }
}