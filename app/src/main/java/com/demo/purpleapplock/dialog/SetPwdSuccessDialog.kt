package com.demo.purpleapplock.dialog

import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_set_pwd_success.*

class SetPwdSuccessDialog(private val clickSure:()->Unit):BaseDialog(R.layout.dialog_set_pwd_success) {
    override fun initView() {
        dialog?.setCancelable(false)
        tv_sure.setOnClickListener {
            clickSure.invoke()
            dismiss()
        }
    }
}