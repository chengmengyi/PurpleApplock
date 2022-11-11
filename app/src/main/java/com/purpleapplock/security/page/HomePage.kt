package com.purpleapplock.security.page

import android.content.Intent
import android.provider.Settings
import com.purpleapplock.security.R
import com.purpleapplock.security.base.BasePage
import com.purpleapplock.security.dialog.SureCancelDialog
import com.purpleapplock.security.enum.PwdTypeEnum
import com.purpleapplock.security.page.lock.SetPwdPage
import com.purpleapplock.security.servers.AppLockServer
import com.purpleapplock.security.util.PwdManager
import com.purpleapplock.security.util.hasLookAppPermission
import com.purpleapplock.security.util.isNoOption
import com.purpleapplock.security.util.showToast
import kotlinx.android.synthetic.main.activity_home.*

class HomePage : BasePage(R.layout.activity_home) {

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
}