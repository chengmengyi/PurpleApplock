package com.demo.purpleapplock.page.server

import android.Manifest
import android.animation.ValueAnimator
import android.content.Intent
import android.net.VpnService
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AlertDialog
import com.demo.purpleapplock.R
import com.demo.purpleapplock.ad.AdManager
import com.demo.purpleapplock.ad.ShowHome
import com.demo.purpleapplock.ad.ShowOpen
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.config.FirebaseConfig
import com.demo.purpleapplock.dialog.IRLimitDialog
import com.demo.purpleapplock.util.*
import com.demo.purpleapplock.vpn.ConnectServerManager
import com.demo.purpleapplock.vpn.IConnectCallback
import com.github.shadowsocks.utils.StartService
import kotlinx.android.synthetic.main.activity_server_home.*

class ServerHomePage:BasePage(R.layout.activity_server_home), IConnectCallback {
    private var canClick=true
    private var permission=false
    private var connect=true
    private var autoConnect=false
    private var connectAnimator:ValueAnimator?=null
    private val show by lazy { ShowHome(AdManager.VPN_HOME,this) }
    private val showConnect by lazy { ShowOpen(AdManager.VPN_CONNECT,this){ jumpToResult() } }

    private val registerResult=registerForActivityResult(StartService()) {
        if (!it && permission) {
            permission = false
            startConnectServer()
        } else {
            canClick=true
            showToast("Connected fail")
        }
    }

    override fun initView() {
        immersionBar.statusBarView(view_top).init()
        ConnectServerManager.init(this,this)
        setServerInfo()
        connect_progress.setOnClickListener {
            if(canClick){
                canClick=false
                clickConnectBtn()
            }
        }

        llc_server_info.setOnClickListener {
            if(canClick){
                startActivityForResult(Intent(this,ChooseServerPage::class.java),1000)
            }
        }

        iv_back.setOnClickListener {
            if (canClick){
                finish()
            }
        }

        autoConnect=intent.getBooleanExtra("connect",false)
        if (autoConnect){
            connect_progress.performClick()
        }
    }

    private fun clickConnectBtn(){
        AdManager.load(AdManager.VPN_RESULT)
        AdManager.load(AdManager.VPN_CONNECT)
        val connected = ConnectServerManager.isConnected()
        if(connected){
            startDisconnectServer()
        }else{
            if(FirebaseConfig.isIR){
                return
            }
            if (!autoConnect){
                SetPointManager.point("function_vpn_click")
            }

            setServerInfo()
            if (getNetStatus()==1){
                showNoNetDialog()
                canClick=true
                return
            }
            if (VpnService.prepare(this) != null) {
                permission = true
                registerResult.launch(null)
                return
            }

            startConnectServer()
        }
    }

    private fun startConnectServer(){
        SetPointManager.point("link_vpn_st")
        SetPointManager.point("authority_user")
        updateConnectingUI()
        ConnectServerManager.connect()
        startConnectAnimator(true)
    }

    private fun startDisconnectServer(){
        updateDisconnectingUI()
        ConnectServerManager.disconnect()
        startConnectAnimator(false)
    }

    private fun startConnectAnimator(connect:Boolean){
        this.connect=connect
        connectAnimator=ValueAnimator.ofInt(0, 100).apply {
            duration=10000L
            interpolator = LinearInterpolator()
            addUpdateListener {
                val p=it.animatedValue as Int
                connect_progress.progress = if (connect) p else 100-p
                val duation = (10 * (p / 100.0F)).toInt()
                if (duation in 2..9){
                    val b = if (connect) ConnectServerManager.isConnected() else ConnectServerManager.isDisconnected()
                    if (b){
                        showConnect.show { jump->
                            connect_progress.progress=100
                            stopConnectAnimator()
                            connectFinish(toResult = jump)
                        }
                    }
                }else if (duation>=10){
                    connectFinish()
                    stopConnectAnimator()
                }
            }
            start()
        }
    }

    private fun connectFinish(toResult:Boolean=true){
        val b = if (connect) ConnectServerManager.isConnected() else ConnectServerManager.isDisconnected()
        if (b){
            if (connect){
                SetPointManager.point("succ_all_link")
                if(autoConnect&&FirebaseConfig.paLog=="B"){
                    AdManager.removeAll()
                }
                updateConnectedUI()
            }else{
                setServerInfo()
                updateDisconnectedUI()
            }
            if (toResult){
                jumpToResult()
            }
        }else{
            SetPointManager.point("fail_all_link")
            updateDisconnectedUI()
            showToast(if (connect) "Connect Fail" else "Disconnect Fail")
        }
        canClick=true
    }

    private fun jumpToResult(){
        if(AcRegister.purpleFront){
            startActivity(Intent(this,ResultPage::class.java).apply {
                putExtra("connect",connect)
            })
        }
    }

    private fun stopConnectAnimator(){
        connectAnimator?.removeAllUpdateListeners()
        connectAnimator?.cancel()
        connectAnimator=null
    }

    private fun updateConnectingUI(){
        tv_connect_status.isSelected=true
        tv_connect_status.text="Status: Connecting"
        iv_btn_connect.setImageResource(R.drawable.btn_connecting)
        iv_connect_status.setImageResource(R.drawable.server_idle)
    }

    private fun updateConnectedUI(){
        tv_connect_status.isSelected=true
        tv_connect_status.text="Status: Connected"
        iv_btn_connect.setImageResource(R.drawable.btn_disconnect)
        iv_connect_status.setImageResource(R.drawable.server_connected)
    }

    private fun updateDisconnectingUI(){
        tv_connect_status.isSelected=false
        tv_connect_status.text="Status: Disconnecting"
        iv_btn_connect.setImageResource(R.drawable.btn_disconnecting)
        iv_connect_status.setImageResource(R.drawable.server_connected)
    }

    private fun updateDisconnectedUI(){
        tv_connect_status.isSelected=false
        tv_connect_status.text="Status: Disconnected"
        iv_btn_connect.setImageResource(R.drawable.btn_connect)
        iv_connect_status.setImageResource(R.drawable.server_idle)
    }

    private fun setServerInfo(){
        val currentServer = ConnectServerManager.currentServer
        tv_server_name.text= getServerName(currentServer)
        iv_server_icon.setImageResource(getSeverIcon(currentServer.kaupunki))
    }

    private fun showNoNetDialog(){
        AlertDialog.Builder(this).apply {
            setMessage("You are not currently connected to the network")
            setPositiveButton("sure", null)
            show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1000){
            when(data?.getStringExtra("result")){
                "dis"->{
                    connect_progress.performClick()
                }
                "con"->{
                    setServerInfo()
                    connect_progress.performClick()
                }
            }
        }
    }

    override fun connectSuccess() {
        updateConnectedUI()
    }

    override fun disconnectSuccess() {
        if (canClick){
            updateDisconnectedUI()
        }
    }

    override fun onResume() {
        super.onResume()
        if(RefreshManager.canRefresh(AdManager.VPN_HOME)){
            show.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        RefreshManager.setBool(AdManager.VPN_HOME,true)
        ConnectServerManager.onDestroy()
        show.stopShow()
    }
}