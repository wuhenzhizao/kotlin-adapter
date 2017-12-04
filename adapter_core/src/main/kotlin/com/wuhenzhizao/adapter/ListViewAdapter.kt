package com.wuhenzhizao.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.wuhenzhizao.adapter.holder.ListViewHolder

/**
 * Created by liufei on 2017/12/4.
 */
open class ListViewAdapter<T : Any>(context: Context, items: List<T>?) : AbsListViewAdapter<T, ListViewHolder>(context, items) {

    constructor(context: Context) : this(context, null)

    override fun getCount(): Int = items.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ListViewHolder
        if (convertView == null) {
            val itemLayoutId = getItemViewType(position)
            val itemView = inflater.inflate(itemLayoutId, parent, false)
            holder = ListViewHolder(itemView)
            onCreateViewHolder(holder)
        } else {
            holder = convertView.tag as ListViewHolder
        }
        onBindViewHolder(position, getItem(position), holder)
        return holder.convertView
    }

    override fun onCreateViewHolder(holder: ListViewHolder) {
        viewHolderCreateInterceptor!!.apply {
            onCreateViewHolder(holder)
        }
    }

    override fun onBindViewHolder(position: Int, item: T, vh: ListViewHolder) {
        viewHolderBindInterceptor!!.apply {
            onBindViewHolder(position, getItem(position), vh)
        }
    }
}