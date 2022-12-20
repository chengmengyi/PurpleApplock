package com.demo.purpleapplock.page

import android.content.Intent
import android.os.Build
import android.provider.Settings
import com.demo.purpleapplock.R
import com.demo.purpleapplock.ad.AdManager
import com.demo.purpleapplock.ad.ShowHome
import com.demo.purpleapplock.ad.ShowLock
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.config.FirebaseConfig
import com.demo.purpleapplock.dialog.IRLimitDialog
import com.demo.purpleapplock.dialog.SureCancelDialog
import com.demo.purpleapplock.dialog.VpnDialog
import com.demo.purpleapplock.enum.PwdTypeEnum
import com.demo.purpleapplock.page.lock.SetPwdPage
import com.demo.purpleapplock.page.server.ChooseServerPage
import com.demo.purpleapplock.page.server.ServerHomePage
import com.demo.purpleapplock.servers.AppLockServer
import com.demo.purpleapplock.util.*
import com.demo.purpleapplock.vpn.ConnectServerManager
import com.github.shadowsocks.bg.BaseService
import com.tencent.mmkv.MMKV
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.*
import java.util.*

class HomePage : BasePage(R.layout.activity_home) {
    private var clickIndex=-1
    private var showingVpnDialog=false
    private val show by lazy { ShowHome(AdManager.HOME,this) }
    private val showHomeClick by lazy { ShowLock(AdManager.HOME_CLICK,this){
        if (it){
            AdManager.load(AdManager.HOME_CLICK)
        }
        if(clickIndex==0){
            jump()
        }else if (clickIndex==1){
            toPlanB(false)
        }
    } }

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
                clickIndex=0
                showHomeClick.show()
            }

        }

        iv_set.setOnClickListener { startActivity(Intent(this,SetPage::class.java)) }

        iv_server.setOnClickListener {
            if(FirebaseConfig.isIR){
                IRLimitDialog().show(supportFragmentManager,"IRLimitDialog")
                return@setOnClickListener
            }
            clickIndex=1
            showHomeClick.show()
        }
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
        if(RefreshManager.canRefresh(AdManager.HOME)){
            show.show()
        }
        checkPaLog()
    }

    private fun checkPaLog(){
        GlobalScope.launch(Dispatchers.Main) {
            delay(300L)
            if(!resume){
                return@launch
            }
            withContext(Dispatchers.Main){
                if(AcRegister.doABPlan){
                    AcRegister.doABPlan=false
                    val referrerStr = getReferrerStr()
                    if(!isBuyUser(referrerStr)){
                        toPlanA()
                        return@withContext
                    }
                    if(FirebaseConfig.paLog.isEmpty()){
                        val paLog = MMKV.defaultMMKV().decodeString("paLog") ?: ""
                        if (paLog.isEmpty()){
                            val nextInt = Random().nextInt(100)
                            FirebaseConfig.paLog=if (nextInt>20) "B" else "A"
                            MMKV.defaultMMKV().encode("oaProgram",paLog)
                        }else{
                            FirebaseConfig.paLog=paLog
                        }
                    }
                    SetPointManager.setUserProperty()
                    if(FirebaseConfig.paLog=="B"&&ConnectServerManager.isDisconnected()){
                        toPlanB(true)
                    }else{
                        toPlanA()
                    }
                }
            }
        }
    }

    private fun toPlanB(auto:Boolean){
        if(FirebaseConfig.isIR){
            IRLimitDialog().show(supportFragmentManager,"IRLimitDialog")
            return
        }
        startActivity(Intent(this,ServerHomePage::class.java).apply {
            putExtra("connect",auto)
        })
    }

    private fun toPlanA(){
        if (FirebaseConfig.paPop=="1"){
            if (AcRegister.loadType==0){
                checkPaRef()
            }
            AcRegister.loadType=2
        }else if (FirebaseConfig.paPop=="0"){
            if (AcRegister.loadType!=2){
                checkPaRef()
            }
            AcRegister.loadType=2
        }
    }

    private fun checkPaRef(){
        when(FirebaseConfig.paRef){
            "0"->showVpnDialog()
            "1"->{
                if (isBuyUser(getReferrerStr())){
                    showVpnDialog()
                }
            }
            "2"->{
                val referrerStr = getReferrerStr()
                if(referrerStr.contains("facebook")||referrerStr.contains("fb4a")){
                    showVpnDialog()
                }
            }
        }
    }

    private fun showVpnDialog(){
        if(ConnectServerManager.isDisconnected()&&!showingVpnDialog){
            showingVpnDialog=true
            VpnDialog{
                showingVpnDialog=false
                if (it){
                    toPlanB(true)
                }
            }.show(supportFragmentManager,"VpnDialog")
        }
    }

    private fun getReferrerStr() = MMKV.defaultMMKV().decodeString("referrer", "")?:""

    override fun onDestroy() {
        super.onDestroy()
        show.stopShow()
    }
}