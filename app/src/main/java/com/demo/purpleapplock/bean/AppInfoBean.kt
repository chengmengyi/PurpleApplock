package com.demo.purpleapplock.bean

import android.graphics.drawable.Drawable

class AppInfoBean(
    var name:String = "",
    var packageName:String = "",
    var icon : Drawable? = null,
    var locked:Boolean=false
) {
}