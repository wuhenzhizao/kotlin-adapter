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
        val itemLayoutId = getItemViewType(position)
        if (convertView == null || (convertView.tag as ListViewHolder).layoutId != itemLayoutId) {
            val itemView = inflater.inflate(itemLayoutId, parent, false)
            holder = ListViewHolder(itemView)
            holder.layoutId = itemLayoutId
            onCreateViewHolder(holder)
        } else {
            holder = convertView.tag as ListViewHolder
        }
        holder.position = position
        onBindViewHolder(position, getItem(position), holder)
        return holder.convertView
    }

    override fun onCreateViewHolder(vh: ListViewHolder) {
        innerHolderCreateInterceptor?.apply {
            onCreateViewHolder(vh)
        }
    }

    override fun onBindViewHolder(position: Int, item: T, vh: ListViewHolder) {
        innerHolderBindInterceptor?.apply {
            onBindViewHolder(position, vh)
        }
        innerClickInterceptor?.apply {
            vh.convertView.setOnClickListener {
                onClick(position, vh)
            }
        }
        innerLongClickInterceptor?.apply {
            vh.convertView.setOnClickListener {
                onLongClick(position, vh)
            }
        }
    }
}