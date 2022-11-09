package com.demo.purpleapplock.page

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.config.LocalConfig
import com.demo.purpleapplock.util.showToast
import kotlinx.android.synthetic.main.activity_set.*
import java.lang.Exception

class SetPage:BasePage(R.layout.activity_set) {
    override fun initView() {
        immersionBar.statusBarView(view_top).init()
        iv_back.setOnClickListener { finish() }
        llc_contact.setOnClickListener {
            try {
                val uri = Uri.parse("mailto:${LocalConfig.EMAIL}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                startActivity(intent)
            }catch (e: Exception){
                showToast("Contact us by email：${LocalConfig.EMAIL}")
            }
        }
        llc_share.setOnClickListener {
            val pm = packageManager
            val packageName=pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(
                Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=${packageName}"
            )
            startActivity(Intent.createChooser(intent, "share"))
        }
        llc_update.setOnClickListener {
            val packName = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES).packageName
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://play.google.com/store/apps/details?id=$packName")
            }
            startActivity(intent)
        }
        llc_privacy.setOnClickListener {
            startActivity(Intent(this,WebPage::class.java))
        }
    }
}