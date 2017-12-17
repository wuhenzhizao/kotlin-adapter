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
        holder.layoutId = viewType
        innerHolderCreateInterceptor?.apply {
            onCreateViewHolder(holder)
        }
        return holder
    }

    override fun onBindViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        innerClickInterceptor?.apply {
            viewHolder.itemView.setOnClickListener {
                onClick(position, viewHolder)
            }
        }
        innerLongClickInterceptor?.apply {
            viewHolder.itemView.setOnClickListener {
                onLongClick(position, viewHolder)
            }
        }
        innerHolderBindInterceptor?.apply {
            onBindViewHolder(position, viewHolder)
        }
    }
}