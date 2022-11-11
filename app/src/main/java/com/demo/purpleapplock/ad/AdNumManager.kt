package com.demo.purpleapplock.ad

import com.tencent.mmkv.MMKV
import java.text.SimpleDateFormat
import java.util.*

object AdNumManager {
    private var maxClick=15
    private var maxShow=50
    private var click=0
    private var show=0

    fun setMax(click:Int,show:Int){
        maxClick=click
        maxShow=show
    }

    fun limit() = click > maxClick||show > maxShow

    fun addClick(){
        click++
        MMKV.defaultMMKV().encode(numKey("click"), click)
    }

    fun addShow(){
        show++
        MMKV.defaultMMKV().encode(numKey("show"), show)
    }

    fun readLocalNum(){
        click= MMKV.defaultMMKV().decodeInt(numKey("click"),0)
        show= MMKV.defaultMMKV().decodeInt(numKey("show"),0)
    }

    private fun numKey(key:String)="${SimpleDateFormat("yyyy-MM-dd").format(Date(System.currentTimeMillis()))}_$key"
}