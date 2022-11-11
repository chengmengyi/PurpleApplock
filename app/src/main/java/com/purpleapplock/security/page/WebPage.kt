package com.purpleapplock.security.page

import com.purpleapplock.security.R
import com.purpleapplock.security.base.BasePage
import com.purpleapplock.security.config.LocalConfig
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