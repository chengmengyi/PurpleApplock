package com.demo.purpleapplock.page

import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.demo.purpleapplock.R
import com.demo.purpleapplock.ad.AdManager
import com.demo.purpleapplock.ad.ShowHome
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.dialog.SureCancelDialog
import com.demo.purpleapplock.enum.PwdTypeEnum
import com.demo.purpleapplock.page.lock.SetPwdPage
import com.demo.purpleapplock.servers.AppLockServer
import com.demo.purpleapplock.util.*
import kotlinx.android.synthetic.main.activity_home.*

class HomePage : BasePage(R.layout.activity_home) {
    private val show by lazy { ShowHome(AdManager.HOME,this) }

    override fun initView() {
        immersionBar.statusBarView(view_top).init()

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(Intent(this,AppLockServer::class.java))
//        }else{
//            startService(Intent(this,AppLockServer::class.java))
//        }
        startService(Intent(this,AppLockServer::class.java))

        iv_lock.setOnClickListener {
            if(!hasLookAppPermission()&&isNoOption()){
                SureCancelDialog(1){
                    val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                    startActivityForResult(intent, 100)
                }.show(supportFragmentManager,"LookAppPermission")
            }else{
                jump()
            }

        }

        iv_set.setOnClickListener { startActivity(Intent(this,SetPage::class.java)) }
    }

    private fun jump(){
        startActivity(Intent(this,SetPwdPage::class.java).apply {
            putExtra("type",if(PwdManager.canJumpAppListPage())PwdTypeEnum.CHECK_PWD else PwdTypeEnum.SET_PWD)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100) {
            if (hasLookAppPermission()) {
                jump()
            } else {
                showToast("No permission")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(AcRegister.refreshNativeAd){
            show.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AcRegister.refreshNativeAd=true
        show.stopShow()
    }
}