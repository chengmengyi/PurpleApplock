package com.demo.purpleapplock.page.lock

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.purpleapplock.R
import com.demo.purpleapplock.adapter.KeyboardAdapter
import com.demo.purpleapplock.adapter.PwdAdapter
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.dialog.PwdFailDialog
import com.demo.purpleapplock.dialog.SetPwdSuccessDialog
import com.demo.purpleapplock.dialog.SureCancelDialog
import com.demo.purpleapplock.enum.PwdTypeEnum
import com.demo.purpleapplock.util.AppManager
import com.demo.purpleapplock.util.PwdManager
import com.demo.purpleapplock.util.showToast
import kotlinx.android.synthetic.main.activity_set_pwd.*

class SetPwdPage:BasePage(R.layout.activity_set_pwd) {
    private var lastPwdStr=""
    private val pwdList= arrayListOf<String>()
    private lateinit var pwdTypeEnum:PwdTypeEnum
    private val pwdAdapter by lazy { PwdAdapter(this) }
    private val keyboardAdapter by lazy { KeyboardAdapter(this){ clickKeyboard(it) } }

    override fun initView() {
        initInfo()
        setAdapter()
        iv_back.setOnClickListener { finish() }
    }

    private fun clickKeyboard(index:Int){
        when(index){
            9->{
                pwdList.clear()
                pwdAdapter.setPwdList(pwdList,false)
                setTitleColor(false)
            }
            11->{
                if(pwdList.isNotEmpty()){
                    pwdList.removeLast()
                    pwdAdapter.setPwdList(pwdList,false)
                    setTitleColor(false)
                }
            }
            else->{
                if(pwdList.size<4){
                    pwdList.add("${index+1}")
                    pwdAdapter.setPwdList(pwdList,false)
                    if(pwdList.size>=4){
                        enterPwdFinish()
                    }
                }
            }
        }
    }

    private fun enterPwdFinish(){
        when(pwdTypeEnum){
            PwdTypeEnum.SET_PWD->{
                startActivity(Intent(this,SetPwdPage::class.java).apply {
                    putExtra("type", PwdTypeEnum.ENTER_PWD_AGAIN)
                    putExtra("pwd",getPwdStr())
                })
            }
            PwdTypeEnum.ENTER_PWD_AGAIN->{
                val pwdStr = getPwdStr()
                if(pwdStr==lastPwdStr){
                    PwdManager.setPwd(pwdStr)
                    SetPwdSuccessDialog{
                        jumpToAllList()
                    }.show(supportFragmentManager,"SetPwdSuccessDialog")
                }else{
                    pwdAdapter.setPwdList(pwdList,true)
                    setTitleColor(true)
                    showToast("The two passwords are inconsistent")
                }
            }
            PwdTypeEnum.CHECK_PWD->{
                val pwdStr = getPwdStr()
                if(PwdManager.checkPwd(pwdStr)){
                    jumpToAllList()
                }else{
                    checkPwdFail()
                }
            }
        }
    }

    private fun checkPwdFail(){
        pwdAdapter.setPwdList(pwdList,true)
        setTitleColor(true)
        PwdManager.addPwdCheckNum()
        if(PwdManager.pwdCheckNum>0){
            PwdFailDialog{
                clickKeyboard(9)
            }.show(supportFragmentManager,"PwdFailDialog")
        }else{
            SureCancelDialog(3){
                SureCancelDialog(4){
                    PwdManager.setPwd("")
                    AppManager.reset()
                    startActivity(Intent(this,SetPwdPage::class.java).apply {
                        putExtra("type",PwdTypeEnum.SET_PWD)
                    })
                    finish()
                }.show(supportFragmentManager,"SureResetPwdDialog")
            }.show(supportFragmentManager,"ResetPwdDialog")
        }
    }

    private fun jumpToAllList(){
        startActivity(Intent(this,AppListPage::class.java))
    }

    private fun getPwdStr():String{
        val stringBuffer = StringBuffer()
        for (it in pwdList) {
            stringBuffer.append(it)
        }
        return stringBuffer.toString()
    }

    private fun setAdapter(){
        rv_keyboard.apply {
            layoutManager=GridLayoutManager(this@SetPwdPage, 3)
            adapter=keyboardAdapter
        }
        rv_pwd.apply {
            layoutManager=LinearLayoutManager(this@SetPwdPage,LinearLayoutManager.HORIZONTAL,false)
            adapter=pwdAdapter
        }
    }

    private fun setTitleColor(red:Boolean){
        tv_title.isSelected=red

    }

    private fun initInfo(){
        immersionBar.statusBarView(view_top).init()
        pwdTypeEnum = intent.getSerializableExtra("type") as PwdTypeEnum
        tv_title.text=when(pwdTypeEnum){
            PwdTypeEnum.SET_PWD->"Set Your Password"
            PwdTypeEnum.ENTER_PWD_AGAIN->{
                lastPwdStr=intent.getStringExtra("pwd")?:""
                "Enter Your Password Again"
            }
            PwdTypeEnum.CHECK_PWD->"Enter Your Password"
        }
    }
}