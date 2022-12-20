package com.demo.purpleapplock.page.server

import com.demo.purpleapplock.R
import com.demo.purpleapplock.ad.AdManager
import com.demo.purpleapplock.ad.ShowHome
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.util.RefreshManager
import com.demo.purpleapplock.util.getSeverIcon
import com.demo.purpleapplock.vpn.ConnectServerManager
import kotlinx.android.synthetic.main.activity_result.*

class ResultPage:BasePage(R.layout.activity_result) {
    private val show by lazy { ShowHome(AdManager.VPN_RESULT,this) }

    override fun initView() {
        immersionBar.statusBarView(view_top).init()

        iv_flag.setImageResource(getSeverIcon(ConnectServerManager.lastServer.kaupunki))
        val connect = intent.getBooleanExtra("connect", false)
        iv_result.setImageResource(if (connect)R.drawable.result_connect else R.drawable.result_disconnect)
        tv_status.text=if (connect) "Connection succeed" else "Disconnection succeed"
        iv_back.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        if(RefreshManager.canRefresh(AdManager.VPN_RESULT)){
            show.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        show.stopShow()
        RefreshManager.setBool(AdManager.VPN_RESULT,true)
    }
}