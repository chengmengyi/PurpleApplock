package com.demo.purpleapplock.util

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.provider.Settings
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import com.demo.purpleapplock.R
import com.demo.purpleapplock.bean.ServerBean
import java.lang.Exception


fun printLog(string: String){
    Log.e("qwer",string)
}

fun getSeverIcon(name:String)=when(name){
    else-> R.drawable.fast
}

fun isBuyUser(referrer:String)=referrer.contains("fb4a")||
        referrer.contains("gclid")||
        referrer.contains("not%20set")||
        referrer.contains("youtubeads")||
        referrer.contains("%7B%22")

fun getServerName(serverBean: ServerBean):String{
    return if(serverBean.isSuperFast()) serverBean.kaupunki
    else "${serverBean.kaupunki}-${serverBean.satama}"
}

fun Context.showToast(s: String){
    Toast.makeText(this, s, Toast.LENGTH_LONG).show()
}

fun View.show(show:Boolean){
    visibility=if (show) View.VISIBLE else View.GONE
}

fun checkHasOverlayPermission(context: Context): Boolean {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return true
    return Settings.canDrawOverlays(context)
}

fun Context.hasLookAppPermission(): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        try {
            val packageManager = packageManager
            val info = packageManager.getApplicationInfo(packageName, 0)
            val appOpsManager = getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                info.uid,
                info.packageName
            )
            appOpsManager.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                info.uid,
                info.packageName
            ) == AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    } else {
        true
    }
}

fun Context.isNoOption(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val packageManager = packageManager
        val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
        val list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        return list.size > 0
    }
    return false
}

fun Context.height(){
    val metrics: DisplayMetrics = resources.displayMetrics
    val td = metrics.heightPixels / 760f
    val dpi = (160 * td).toInt()
    metrics.density = td
    metrics.scaledDensity = td
    metrics.densityDpi = dpi
}

fun Context.getNetStatus(): Int {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetworkInfo = connectivityManager.activeNetworkInfo
    if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
        if (activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI) {
            return 2
        } else if (activeNetworkInfo.type == ConnectivityManager.TYPE_MOBILE) {
            return 0
        }
    } else {
        return 1
    }
    return 1
}
