package com.demo.purpleapplock.util

object RefreshManager {
    private val map= hashMapOf<String,Boolean>()

    fun canRefresh(type:String)=map[type]?:true

    fun setBool(type: String,boolean: Boolean){
        map[type]=boolean
    }

    fun resetAll(){
        map.keys.forEach { map[it]=true }
    }
}