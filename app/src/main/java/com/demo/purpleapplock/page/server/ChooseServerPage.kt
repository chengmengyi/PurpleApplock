package com.demo.purpleapplock.page.server

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.purpleapplock.R
import com.demo.purpleapplock.ad.AdManager
import com.demo.purpleapplock.ad.ShowLock
import com.demo.purpleapplock.ad.ShowOpen
import com.demo.purpleapplock.adapter.ServerAdapter
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.bean.ServerBean
import com.demo.purpleapplock.vpn.ConnectServerManager
import kotlinx.android.synthetic.main.activity_choose_server.*

class ChooseServerPage:BasePage(R.layout.activity_choose_server) {
    private val showBack by lazy { ShowLock(AdManager.VPN_BACK,this){finish()} }
//    private val showConnect by lazy { ShowLock(AdManager.VPN_CONNECT,this){ clickItem() } }

    override fun initView() {
        immersionBar.statusBarView(view_top).init()
        AdManager.load(AdManager.VPN_BACK)
        AdManager.load(AdManager.VPN_CONNECT)
        iv_back.setOnClickListener { onBackPressed() }

        rv_server.apply {
            layoutManager=LinearLayoutManager(this@ChooseServerPage)
            adapter=ServerAdapter(this@ChooseServerPage){
                clickItem(it)
            }
        }
    }

    private fun clickItem(serverBean: ServerBean){
        val currentServer = ConnectServerManager.currentServer
        val connected = ConnectServerManager.isConnected()
        if (connected&&currentServer.ip!=serverBean.ip){
            showDialog { chooseServer("dis",serverBean) }
        }else{
            if (connected){
                chooseServer("",serverBean)
            }else{
                chooseServer("con",serverBean)
            }
        }
    }

    private fun chooseServer(result:String,serverBean: ServerBean){
        ConnectServerManager.currentServer=serverBean
        setResult(1000, Intent().apply {
            putExtra("result",result)
        })
        finish()
    }

    private fun showDialog(back:()->Unit){
        AlertDialog.Builder(this).apply {
            setMessage("You are currently connected and need to disconnect before manually connecting to the server.")
            setPositiveButton("sure") { _, _ ->
                back.invoke()
            }
            setNegativeButton("cancel",null)
            show()
        }
    }

    override fun onBackPressed() {
        showBack.show()
    }
}