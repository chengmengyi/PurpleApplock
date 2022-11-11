package com.demo.purpleapplock.bean

class AdBean(
    val ad:Any?=null,
    val t:Long=0
) {
    fun expired()=(System.currentTimeMillis() - t) >=3600000L
}