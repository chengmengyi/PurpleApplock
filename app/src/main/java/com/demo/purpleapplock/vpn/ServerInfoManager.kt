package com.demo.purpleapplock.vpn

import com.demo.purpleapplock.bean.ServerBean
import com.demo.purpleapplock.config.FirebaseConfig
import com.demo.purpleapplock.config.LocalConfig

object ServerInfoManager {
    
    fun getAllServerList()=FirebaseConfig.serverList.ifEmpty { LocalConfig.localServerList }

    fun getFastServer():ServerBean{
        val serverList = getAllServerList()
        if (!FirebaseConfig.cityList.isNullOrEmpty()){
            val filter = serverList.filter { FirebaseConfig.cityList.contains(it.kaupunki) }
            if (!filter.isNullOrEmpty()){
                return filter.random()
            }
        }
        return serverList.random()
    }
}
