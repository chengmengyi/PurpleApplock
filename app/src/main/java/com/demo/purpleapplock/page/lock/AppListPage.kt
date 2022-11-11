package com.demo.purpleapplock.page.lock

import android.content.Intent
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.demo.purpleapplock.R
import com.demo.purpleapplock.ad.AdManager
import com.demo.purpleapplock.ad.ShowHome
import com.demo.purpleapplock.adapter.ViewPagerAdapter
import com.demo.purpleapplock.base.BasePage
import com.demo.purpleapplock.interfaces.IUpdateAppList
import com.demo.purpleapplock.page.HomePage
import com.demo.purpleapplock.page.lock.fragment.InstalledFragment
import com.demo.purpleapplock.page.lock.fragment.LockedFragment
import kotlinx.android.synthetic.main.activity_app_list.*

class AppListPage: BasePage(R.layout.activity_app_list),IUpdateAppList {
    private val list= arrayListOf<Fragment>()
    private val show by lazy { ShowHome(AdManager.LOCK_HOME,this) }

    override fun initView() {
        immersionBar.statusBarView(view_top).init()
        clickTitle(0)
        setAdapter()
        tv_locked.setOnClickListener {
            clickTitle(0)
            viewpager.currentItem=0
        }
        tv_installed.setOnClickListener {
            clickTitle(1)
            viewpager.currentItem=1
        }
        iv_back.setOnClickListener { back() }
    }

    private fun setAdapter(){
        list.add(LockedFragment())
        list.add(InstalledFragment())
        viewpager.adapter= ViewPagerAdapter(list,supportFragmentManager)
        viewpager.addOnPageChangeListener(
            object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    clickTitle(position)
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            }
        )
    }

    private fun clickTitle(index:Int){
        tv_locked.isSelected=index==0
        tv_installed.isSelected=index==1
    }

    private fun back(){
        startActivity(Intent(this,HomePage::class.java))
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode== KeyEvent.KEYCODE_BACK){
            back()
            return true
        }
        return false
    }

    override fun updateLockedList() {
        try {
            val fragment = list[0]
            if (fragment is LockedFragment){
                fragment.updateList()
            }
        }catch (e:Exception){

        }
    }

    override fun updateInstalledList() {
        try {
            val fragment = list[1]
            if (fragment is InstalledFragment){
                fragment.updateList()
            }
        }catch (e:Exception){

        }
    }

    override fun onResume() {
        super.onResume()
        show.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        show.stopShow()
    }
}