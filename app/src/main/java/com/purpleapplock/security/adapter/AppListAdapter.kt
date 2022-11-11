package com.purpleapplock.security.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.purpleapplock.security.R
import com.purpleapplock.security.bean.AppInfoBean
import kotlinx.android.synthetic.main.layout_app_item.view.*

class AppListAdapter(
    private val context: Context,
    private val list:ArrayList<AppInfoBean>,
    private val click:(bean:AppInfoBean)->Unit
):RecyclerView.Adapter<AppListAdapter.MyView>() {
    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                click.invoke(list[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(context).inflate(R.layout.layout_app_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            val appInfoBean = list[position]
            iv_icon.setImageDrawable(appInfoBean.icon)
            tv_title.text=appInfoBean.name
            iv_suo.setImageResource(if(appInfoBean.locked) R.drawable.suo1 else R.drawable.suo2)
        }
    }

    override fun getItemCount(): Int = list.size
}