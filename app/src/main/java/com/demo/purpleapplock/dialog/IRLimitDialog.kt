package com.demo.purpleapplock.dialog

import com.demo.purpleapplock.R
import com.demo.purpleapplock.base.BaseDialog
import kotlinx.android.synthetic.main.dialog_ir_limit.*

class IRLimitDialog:BaseDialog(R.layout.dialog_ir_limit) {
    override fun initView() {
        dialog?.setCancelable(false)
        tv_sure.setOnClickListener {
            dismiss()
        }
    }
}