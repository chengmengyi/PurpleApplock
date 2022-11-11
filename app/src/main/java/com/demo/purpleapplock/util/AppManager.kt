package com.demo.purpleapplock.util

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.collection.arraySetOf
import com.demo.purpleapplock.bean.AppInfoBean
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object AppManager {
    private val lockedAppNameList = arraySetOf<String>()
    val lockedAppList= arrayListOf<AppInfoBean>()
    val unLockAppList= arrayListOf<AppInfoBean>()

    fun getInstalledAppList(context: Context){
        getLockedAppList()
        val packageName = context.packageName
        GlobalScope.launch {
            val packageManager: PackageManager = context.packageManager
            val list = packageManager.getInstalledPackages(0)
            unLockAppList.clear()
            lockedAppList.clear()
            for (p in list) {
                val bean = AppInfoBean()
                bean.icon = p.applicationInfo.loadIcon(packageManager)
                bean.name=packageManager.getApplicationLabel(p.applicationInfo).toString()
                bean.packageName=p.applicationInfo.packageName
                bean.locked=lockedAppNameList.contains(bean.packageName)
                val flags = p.applicationInfo.flags
                if (flags and ApplicationInfo.FLAG_SYSTEM != 0) {

                } else {
                    if (bean.packageName!=packageName){
                        if (bean.locked){
                            lockedAppList.add(bean)
                        }else{
                            unLockAppList.add(bean)
                        }
                    }
                }
            }
        }
    }

    fun clickApp(appInfoBean: AppInfoBean){
        if(appInfoBean.locked){
            appInfoBean.locked=!appInfoBean.locked
            lockedAppNameList.remove(appInfoBean.packageName)
            lockedAppList.remove(appInfoBean)
            unLockAppList.add(0,appInfoBean)
        }else{
            appInfoBean.locked=!appInfoBean.locked
            lockedAppNameList.add(appInfoBean.packageName)
            lockedAppList.add(0,appInfoBean)
            unLockAppList.remove(appInfoBean)
        }
        saveLockedAppList()
    }

    private fun getLockedAppList(){
        val locked=MMKV.defaultMMKV().decodeStringSet("locked")
        if(null!=locked){
            lockedAppNameList.addAll(locked)
        }
    }

    private fun saveLockedAppList(){
        MMKV.defaultMMKV().encode("locked",lockedAppNameList)
    }

    fun checkAppLocked(name:String)=lockedAppNameList.contains(name)

    fun reset(){
        lockedAppNameList.clear()
        saveLockedAppList()
        unLockAppList.addAll(lockedAppList)
        unLockAppList.forEach {
            it.locked=false
        }
        lockedAppList.clear()
    }
}