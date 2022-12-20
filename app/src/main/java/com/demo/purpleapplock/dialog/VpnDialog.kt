package com.demo.purpleapplock.dialog

import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BaseDialog
import com.demo.purpleapplock.util.SetPointManager
import kotlinx.android.synthetic.main.dialog_vpn.*

class VpnDialog(private val sureClick:(sure:Boolean)->Unit):BaseDialog(R.layout.dialog_vpn) {
    override fun initView() {
        dialog?.setCancelable(false)
        SetPointManager.point("show_pop")

        iv_cancel.setOnClickListener {
            SetPointManager.point("click_close_pop")
            dismiss()
            sureClick.invoke(false)
        }

        tv_ok.setOnClickListener {
            SetPointManager.point("click_link_pop")
            dismiss()
            sureClick.invoke(true)
        }
    }
}