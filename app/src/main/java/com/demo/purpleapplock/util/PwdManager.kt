package com.demo.purpleapplock.util

import com.tencent.mmkv.MMKV

object PwdManager {
    var pwdCheckNum=3
    var lockClickNum=0

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

    fun checkCanShowLockAd():Boolean{
        val b = lockClickNum % 5 == 0
        printLog("kkkkk==$b===${lockClickNum}")
        return b
    }
}