package com.demo.purpleapplock.page.lock.fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.purpleapplock.R
import com.demo.purpleapplock.adapter.AppListAdapter
import com.demo.purpleapplock.bean.AppInfoBean
import com.demo.purpleapplock.dialog.SureCancelDialog
import com.demo.purpleapplock.interfaces.IUpdateAppList
import com.demo.purpleapplock.util.AppManager
import com.demo.purpleapplock.util.checkHasOverlayPermission
import kotlinx.android.synthetic.main.fragment_installed.*

class InstalledFragment:Fragment() {
    private var iUpdateAppList:IUpdateAppList?=null
    private val appListAdapter by lazy { AppListAdapter(requireContext(),AppManager.unLockAppList){ clickItem(it) } }

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
        return inflater.inflate(R.layout.fragment_installed,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
    }

    private fun clickItem(appInfoBean: AppInfoBean){
        if(!checkHasOverlayPermission(requireContext())){
            SureCancelDialog(2){
                val intent=Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${requireContext().packageName}"))
                startActivityForResult(intent, 101)
            }.show(childFragmentManager,"OverlayPermission")
            return
        }

        AppManager.clickApp(appInfoBean)
//        appInfoBean.locked=!appInfoBean.locked
        appListAdapter.notifyDataSetChanged()
        iUpdateAppList?.updateLockedList()
    }

    private fun setAdapter(){
        rv_install.apply {
            layoutManager= LinearLayoutManager(requireContext())
            adapter=appListAdapter
        }
    }

    fun updateList(){
        appListAdapter.notifyDataSetChanged()
    }
}