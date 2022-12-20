package com.demo.purpleapplock.bean

import com.github.shadowsocks.database.Profile
import com.github.shadowsocks.database.ProfileManager

class ServerBean(
    val tilinumero:String="", //账号
    val satama:Int=0, //端口
    val salasana:String="",  //密码
    val kaupunki:String="Super Fast Servers", //国家
    val maa:String="", //城市
    val ip:String="" //ip
) {
    fun isSuperFast()=kaupunki=="Super Fast Servers"&&ip.isEmpty()

    fun getServerId():Long{
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.host==ip&&it.remotePort==satama){
                return it.id
            }
        }
        return 0L
    }

    fun writeServerId(){
        val profile = Profile(
            id = 0L,
            name = "$kaupunki - $maa",
            host = ip,
            remotePort = satama,
            password = salasana,
            method = tilinumero
        )

        var id:Long?=null
        ProfileManager.getActiveProfiles()?.forEach {
            if (it.remotePort==profile.remotePort&&it.host==profile.host){
                id=it.id
                return@forEach
            }
        }
        if (null==id){
            ProfileManager.createProfile(profile)
        }else{
            profile.id=id!!
            ProfileManager.updateProfile(profile)
        }
    }
}