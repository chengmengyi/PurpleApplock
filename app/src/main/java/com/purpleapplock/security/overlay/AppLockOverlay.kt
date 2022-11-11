package com.purpleapplock.security.overlay

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.purpleapplock.security.R
import com.purpleapplock.security.adapter.KeyboardAdapter
import com.purpleapplock.security.adapter.PwdAdapter
import com.purpleapplock.security.util.PwdManager
import com.purpleapplock.security.util.height
import com.purpleapplock.security.util.show

@SuppressLint("StaticFieldLeak")
object AppLockOverlay {
    private var showing=false
    private val pwdList= arrayListOf<String>()
    private lateinit var view: View
    private lateinit var tvTitle: AppCompatTextView
    private lateinit var windowManager: WindowManager
    private lateinit var layoutParams: WindowManager.LayoutParams
    private lateinit var pwdAdapter:PwdAdapter
    private lateinit var keyboardAdapter: KeyboardAdapter

    fun initOverlay(context: Context){
        initView(context)
        setAdapter(context)
    }

    private fun initView(context: Context) {
        windowManager = context.getSystemService(Service.WINDOW_SERVICE) as WindowManager
        layoutParams = WindowManager.LayoutParams()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE
        }
        //        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        layoutParams.format = PixelFormat.RGBA_8888
        layoutParams.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams.flags = (WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                or WindowManager.LayoutParams.FLAG_FULLSCREEN
                or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                )
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT


        context.height()
        view = LayoutInflater.from(context).inflate(R.layout.activity_set_pwd, null)
        tvTitle=view.findViewById(R.id.tv_title)
        view.findViewById<AppCompatImageView>(R.id.iv_back).show(false)
        tvTitle.text="Enter Your Password"
    }

    private fun setAdapter(context: Context) {
        val rv_pwd = view.findViewById<RecyclerView>(R.id.rv_pwd)
        val rv_keyboard = view.findViewById<RecyclerView>(R.id.rv_keyboard)
        pwdAdapter= PwdAdapter(context)
        keyboardAdapter=KeyboardAdapter(context){
            clickKeyboard(it)
        }
        rv_pwd.apply {
            layoutManager= LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
            adapter= pwdAdapter
        }
        rv_keyboard.apply {
            layoutManager= GridLayoutManager(context,3)
            adapter=keyboardAdapter
        }
    }

    private fun clickKeyboard(index:Int){
        when(index){
            9->{
                pwdList.clear()
                pwdAdapter.setPwdList(pwdList,false)
                setTitleColor(false)
            }
            11->{
                if(pwdList.isNotEmpty()){
                    pwdList.removeLast()
                    pwdAdapter.setPwdList(pwdList,false)
                    setTitleColor(false)
                }
            }
            else->{
                if(pwdList.size<4){
                    pwdList.add("${index+1}")
                    pwdAdapter.setPwdList(pwdList,false)
                    if(pwdList.size>=4){
                        val pwdStr = getPwdStr()
                        if(PwdManager.checkPwd(pwdStr)){
                            tvTitle.text="Enter Your Password"
                            pwdList.clear()
                            pwdAdapter.setPwdList(pwdList,false)
                            hide()
                        }else{
                            tvTitle.text="Password error"
                            setTitleColor(true)
                        }
                    }
                }
            }
        }
    }

    private fun getPwdStr():String{
        val stringBuffer = StringBuffer()
        for (it in pwdList) {
            stringBuffer.append(it)
        }
        return stringBuffer.toString()
    }

    private fun setTitleColor(red:Boolean){
        tvTitle.isSelected=red
    }

    fun show(){
        if (!showing){
            showing=true
            pwdList.clear()
            tvTitle.text="Enter Your Password"
            pwdAdapter.setPwdList(pwdList,false)
            setTitleColor(false)
            windowManager.addView(view, layoutParams)
        }
    }

    fun hide(){
        if (showing){
            showing=false
            windowManager.removeView(view)
        }
    }
}