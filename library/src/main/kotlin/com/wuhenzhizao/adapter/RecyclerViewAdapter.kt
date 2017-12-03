package com.wuhenzhizao.adapter

import android.content.Context
import android.view.ViewGroup
import com.wuhenzhizao.adapter.extentions.getItem
import com.wuhenzhizao.adapter.holder.ViewHolder

/**
 * Created by liufei on 2017/12/3.
 */
class RecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : AbsRecyclerViewAdapter<T, ViewHolder>(context, items) {

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val itemView = inflater.inflate(viewType, parent, false)
        val holder = ViewHolder(itemView)
        viewHolderCreateInterceptor!!.apply {
            onCreateViewHolder(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        clickInterceptor!!.apply {
            holder.itemView.setOnClickListener {
                onClick(position, getItem(position), holder)
            }
        }
        longClickInterceptor!!.apply {
            holder.itemView.setOnClickListener {
                onLongClick(position, getItem(position), holder)
            }
        }
        viewHolderBindInterceptor!!.apply {
            onBindViewHolder(position, getItem(position), holder)
        }
    }
}