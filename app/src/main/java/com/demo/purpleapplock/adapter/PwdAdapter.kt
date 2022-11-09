package com.demo.purpleapplock.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.purpleapplock.R
import kotlinx.android.synthetic.main.layout_pwd_item.view.*

class PwdAdapter(private val context: Context):RecyclerView.Adapter<PwdAdapter.MyView>() {
    private var currentPwdLength=0
    private var enterFail=false

    fun setPwdList(list:ArrayList<String>,fail:Boolean){
        currentPwdLength=list.size
        enterFail=fail
        notifyDataSetChanged()
    }

    inner class MyView(view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(context).inflate(R.layout.layout_pwd_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            if (enterFail){
                iv_pwd.setImageResource(R.drawable.pwd3)
            }else{
                iv_pwd.setImageResource(if (currentPwdLength>=(position+1)) R.drawable.pwd2 else R.drawable.pwd1)
            }
        }
    }

    override fun getItemCount(): Int = 4
}