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
        val viewType = getItemViewType(position)
        if (convertView == null || (convertView.tag as ListViewHolder).viewType != viewType) {
            val itemView = inflater.inflate(viewType, parent, false)
            holder = ListViewHolder(itemView, viewType)
            onCreateViewHolder(holder)
        } else {
            holder = convertView.tag as ListViewHolder
        }
        holder.position = position
        onBindViewHolder(position, getItem(position), holder)
        return holder.convertView
    }

    override fun onCreateViewHolder(vh: ListViewHolder) {
        innerHolderCreateListener?.apply {
            onCreateViewHolder(vh)
        }
    }

    override fun onBindViewHolder(position: Int, item: T, vh: ListViewHolder) {
        innerHolderBindListener?.apply {
            onBindViewHolder(vh, position, null)
        }
        innerClickListener?.apply {
            vh.convertView.setOnClickListener {
                onClick(vh, position)
            }
        }
        innerLongClickListener?.apply {
            vh.convertView.setOnLongClickListener {
                onLongClick(vh, position)
            }
        }
    }
}