package com.demo.purpleapplock.dialog

import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_permission.*

//1查看app使用情况 2浮窗权限  3是否重置密码 4确认重置
class SureCancelDialog(
    private val type:Int,
    private val clickSure:()->Unit
):BaseDialog(R.layout.dialog_permission) {
    override fun initView() {
        dialog?.setCancelable(false)
        tv_content.setText(when(type){
            1->R.string.look_app_permission
            2->R.string.overlay_permission
            3->R.string.reset_pwd
            else->R.string.sure_reset
        })
        tv_cancel.setOnClickListener { dismiss() }

        tv_sure.setOnClickListener {
            clickSure.invoke()
            dismiss()
        }
    }
}