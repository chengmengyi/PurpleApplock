package com.purpleapplock.security.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.purpleapplock.security.R
import kotlinx.android.synthetic.main.layout_keyboard_item.view.*

class KeyboardAdapter(
    private val context:Context,
    private val click:(index:Int)->Unit
):RecyclerView.Adapter<KeyboardAdapter.MyView>() {
    private val list= arrayListOf<Int>()
    init {
        list.add(R.drawable.key1)
        list.add(R.drawable.key2)
        list.add(R.drawable.key3)
        list.add(R.drawable.key4)
        list.add(R.drawable.key5)
        list.add(R.drawable.key6)
        list.add(R.drawable.key7)
        list.add(R.drawable.key8)
        list.add(R.drawable.key9)
        list.add(R.drawable.key10)
        list.add(R.drawable.key11)
        list.add(R.drawable.key12)
    }

    inner class MyView(view:View):RecyclerView.ViewHolder(view){
        init {
            view.setOnClickListener {
                click.invoke(layoutPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        return MyView(LayoutInflater.from(context).inflate(R.layout.layout_keyboard_item,parent,false))
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        with(holder.itemView){
            iv_keyboard.setImageResource(list[position])
        }
    }

    override fun getItemCount(): Int = list.size
}