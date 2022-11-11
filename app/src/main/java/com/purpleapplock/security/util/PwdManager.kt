package com.purpleapplock.security.util

import com.tencent.mmkv.MMKV

object PwdManager {
    var pwdCheckNum=3

    fun canJumpAppListPage()= getPwd().isNotEmpty()

    fun setPwd(pwd:String){
        pwdCheckNum=3
        MMKV.defaultMMKV().encode("pwd",pwd)
    }

    fun checkPwd(pwd:String):Boolean = getPwd() == pwd

    private fun getPwd()=MMKV.defaultMMKV().decodeString("pwd")?:""

    fun addPwdCheckNum(){
        pwdCheckNum--
    }
}