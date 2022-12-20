package com.demo.purpleapplock.vpn

import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.bean.ServerBean
import com.github.shadowsocks.Core
import com.github.shadowsocks.aidl.IShadowsocksService
import com.github.shadowsocks.aidl.ShadowsocksConnection
import com.github.shadowsocks.bg.BaseService
import com.github.shadowsocks.preference.DataStore
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object ConnectServerManager: ShadowsocksConnection.Callback {
    private var basePage:BasePage?=null
    private var state = BaseService.State.Stopped
    var currentServer=ServerBean()
    var lastServer=ServerBean()
    private val sc= ShadowsocksConnection(true)
    private var iConnectCallback:IConnectCallback?=null

    fun init(basePage: BasePage,iConnectCallback:IConnectCallback){
        this.basePage=basePage
        this.iConnectCallback=iConnectCallback
        sc.connect(basePage,this)
    }

    fun connect(){
        state= BaseService.State.Connecting
        GlobalScope.launch {
            if (currentServer.isSuperFast()){
                DataStore.profileId = ServerInfoManager.getFastServer().getServerId()
            }else{
                DataStore.profileId = currentServer.getServerId()
            }
            Core.startService()
        }
    }

    fun disconnect(){
        state= BaseService.State.Stopping
        GlobalScope.launch {
            Core.stopService()
        }
    }

    fun isConnected()= state==BaseService.State.Connected

    fun isDisconnected()= state==BaseService.State.Stopped

    override fun stateChanged(state: BaseService.State, profileName: String?, msg: String?) {
        this.state=state
        if (isConnected()){
            lastServer= currentServer
        }
        if (isDisconnected()){
            iConnectCallback?.disconnectSuccess()
        }
    }

    override fun onServiceConnected(service: IShadowsocksService) {
        val state = BaseService.State.values()[service.state]
        this.state=state
        if (isConnected()){
            lastServer= currentServer
            iConnectCallback?.connectSuccess()
        }
    }

    override fun onBinderDied() {
        basePage?.let {
            sc.disconnect(it)
        }
    }

    fun onDestroy(){
        onBinderDied()
        basePage=null
        iConnectCallback=null
    }

}