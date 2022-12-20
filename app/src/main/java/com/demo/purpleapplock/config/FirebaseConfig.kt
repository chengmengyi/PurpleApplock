package com.demo.purpleapplock.config

import com.demo.purpleapplock.ad.AdNumManager
import com.demo.purpleapplock.bean.ServerBean
import com.demo.purpleapplock.util.printLog
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.lzy.okgo.OkGo
import com.lzy.okgo.callback.StringCallback
import com.lzy.okgo.model.Response
import com.tencent.mmkv.MMKV
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

object FirebaseConfig {
    var paLog=""
    var paRef="1"
    var paPop="1"
    var isIR=false
    val serverList= arrayListOf<ServerBean>()
    val cityList= arrayListOf<String>()

    fun readFireConfig(){
        LocalConfig.localServerList.forEach { it.writeServerId() }

//        val remoteConfig = Firebase.remoteConfig
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            if (it.isSuccessful){
//                readAdConfig(remoteConfig.getString("purple_ad"))
//                readServerConfig(remoteConfig.getString("pa_server_c"))
//                readCityConfig(remoteConfig.getString("pa_direct"))
//
//                val pa_log = remoteConfig.getString("pa_log")
//                if(pa_log.isNotEmpty()){
//                    paLog=pa_log
//                }
//
//                val pa_ref = remoteConfig.getString("pa_ref")
//                if(pa_ref.isNotEmpty()){
//                    paRef=pa_ref
//                }
//
//                val pa_pop = remoteConfig.getString("pa_pop")
//                if(pa_pop.isNotEmpty()){
//                    paPop=pa_pop
//                }
//            }
//        }
    }

    private fun readServerConfig(string: String){
        try {
            val jsonArray = JSONObject(string).getJSONArray("pa_server_c")
            for (index in 0 until jsonArray.length()){
                val jsonObject = jsonArray.getJSONObject(index)
                serverList.add(
                    ServerBean(
                        tilinumero = jsonObject.optString("tilinumero"),
                        satama = jsonObject.optInt("satama"),
                        salasana=jsonObject.optString("salasana"),
                        kaupunki=jsonObject.optString("kaupunki"),
                        maa=jsonObject.optString("maa"),
                        ip=jsonObject.optString("ip")
                    )
                )
            }
            serverList.forEach { it.writeServerId() }
        }catch (e:Exception){

        }
    }

    private fun readCityConfig(string: String){
        try {
            val jsonArray = JSONArray(string)
            for (index in 0 until jsonArray.length()){
                cityList.add(jsonArray.optString(index))
            }
        }catch (e:Exception){

        }
    }

    private fun readAdConfig(string: String){
        try {
            val jsonObject = JSONObject(string)
            AdNumManager.setMax(jsonObject.optInt("max_click"),jsonObject.optInt("max_show"))
            MMKV.defaultMMKV().encode("purple_ad",string)
        }catch (e:Exception){

        }
    }

    fun getAdConfig():String{
        val purpleAd=MMKV.defaultMMKV().decodeString("purple_ad")?:""
        return if (purpleAd.isEmpty()) LocalConfig.localAd else purpleAd
    }

    fun checkIR(){
        val country = Locale.getDefault().country
        if(country=="IR"){
            isIR=true
        }else{
            OkGo.get<String>("https://api.myip.com/")
                .execute(object : StringCallback(){
                    override fun onSuccess(response: Response<String>?) {
//                        ipJson="""{"ip":"89.187.185.11","country":"United States","cc":"IR"}"""
                        try {
                            isIR=JSONObject(response?.body()?.toString()).optString("cc")=="IR"
                        }catch (e:Exception){

                        }
                    }

                    override fun onError(response: Response<String>?) {
                        super.onError(response)
                    }
                })
        }
    }
}