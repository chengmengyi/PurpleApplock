package com.demo.purpleapplock.dialog

import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BaseDialog
import com.demo.purpleapplock.util.PwdManager
import kotlinx.android.synthetic.main.dialog_pwd_fail.*
import kotlinx.android.synthetic.main.dialog_set_pwd_success.*
import kotlinx.android.synthetic.main.dialog_set_pwd_success.tv_sure

class PwdFailDialog(private val closeCallback:()->Unit):BaseDialog(R.layout.dialog_pwd_fail) {
    override fun initView() {
        dialog?.setCancelable(false)
        tv_content.text="Gesture password is wrong, you can try ${PwdManager.pwdCheckNum} more times!"
        tv_sure.setOnClickListener {
            closeCallback.invoke()
            dismiss()
        }
    }
}