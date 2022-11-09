package com.demo.purpleapplock.page.lock.fragment

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ImageSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.purpleapplock.R
import com.demo.purpleapplock.adapter.AppListAdapter
import com.demo.purpleapplock.bean.AppInfoBean
import com.demo.purpleapplock.interfaces.IUpdateAppList
import com.demo.purpleapplock.util.AppManager
import com.demo.purpleapplock.util.show
import kotlinx.android.synthetic.main.fragment_locked.*

class LockedFragment:Fragment() {
    private var iUpdateAppList: IUpdateAppList?=null
    private val appListAdapter by lazy { AppListAdapter(requireContext(),AppManager.lockedAppList){ clickItem(it) } }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            iUpdateAppList=context as IUpdateAppList
        }catch (e:Exception){

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_locked,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTips()
        setAdapter()
    }

    private fun clickItem(appInfoBean: AppInfoBean){
        AppManager.clickApp(appInfoBean)
        appListAdapter.notifyDataSetChanged()
        iUpdateAppList?.updateInstalledList()
    }

    private fun setAdapter(){
        rv_locked.apply {
            layoutManager=LinearLayoutManager(requireContext())
            adapter=appListAdapter
        }
    }

    fun updateList(){
        appListAdapter.notifyDataSetChanged()
        setTips()
    }

    private fun setTips(){
        if (AppManager.lockedAppList.isNotEmpty()){
            tv_tips.show(false)
            return
        }
        var str="Enter Installed Apps list, and click % of apps to lock it now!"
        val indexOf = str.indexOf("% ")
        val spannableString= SpannableString(str)
        val d = ContextCompat.getDrawable(requireContext(),R.drawable.suo2)!!
        d.setBounds(0, 0, d.minimumWidth, d.minimumHeight)
        spannableString.setSpan(
            ImageSpan(d),
            indexOf,
            indexOf+1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        tv_tips.text=spannableString
        tv_tips.movementMethod = LinkMovementMethod.getInstance()
    }
}