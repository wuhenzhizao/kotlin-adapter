package com.wuhenzhizao.adapter

import android.content.Context
import android.view.ViewGroup
import com.wuhenzhizao.adapter.extension.getItem
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder

/**
 * Created by liufei on 2017/12/3.
 */
open class RecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : AbsRecyclerViewAdapter<T, RecyclerViewHolder>(context, items) {

    constructor(context: Context) : this(context, null)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {
        val itemView = inflater.inflate(viewType, parent, false)
        val holder = RecyclerViewHolder(itemView)
        viewHolderCreateInterceptor!!.apply {
            onCreateViewHolder(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holderRecycler: RecyclerViewHolder, position: Int) {
        clickInterceptor!!.apply {
            holderRecycler.itemView.setOnClickListener {
                onClick(position, getItem(position), holderRecycler)
            }
        }
        longClickInterceptor!!.apply {
            holderRecycler.itemView.setOnClickListener {
                onLongClick(position, getItem(position), holderRecycler)
            }
        }
        viewHolderBindInterceptor!!.apply {
            onBindViewHolder(position, getItem(position), holderRecycler)
        }
    }
}