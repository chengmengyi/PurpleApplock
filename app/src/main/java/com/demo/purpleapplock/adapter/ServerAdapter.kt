package com.demo.purpleapplock.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.demo.purpleapplock.R
import com.demo.purpleapplock.bean.ServerBean
import com.demo.purpleapplock.util.getServerName
import com.demo.purpleapplock.util.getSeverIcon
import com.demo.purpleapplock.vpn.ConnectServerManager
import com.demo.purpleapplock.vpn.ServerInfoManager
import kotlinx.android.synthetic.main.item_server.view.*

class ServerAdapter(
    private val context:Context,
    private val click:(bean:ServerBean)->Unit
):RecyclerView.Adapter<ServerAdapter.ServerView>() {
    private val list= arrayListOf<ServerBean>()
    init {
        list.add(ServerBean())
        list.addAll(ServerInfoManager.getAllServerList())
    }

    inner class ServerView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                click.invoke(list[layoutPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServerView {
        return ServerView(LayoutInflater.from(context).inflate(R.layout.item_server,parent,false))
    }

    override fun onBindViewHolder(holder: ServerView, position: Int) {
        with(holder.itemView){
            val serverBean = list[position]
            tv_server_name.text= getServerName(serverBean)
            iv_server_icon.setImageResource(getSeverIcon(serverBean.kaupunki))
            iv_choose.isSelected=serverBean.ip==ConnectServerManager.currentServer.ip
        }
    }

    override fun getItemCount(): Int = list.size
}