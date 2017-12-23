package com.wuhenzhizao.adapter

import android.content.Context
import android.view.ViewGroup
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder

/**
 * Created by liufei on 2017/12/3.
 */
open class RecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : AbsRecyclerViewAdapter<T, RecyclerViewHolder>(context, items) {

    constructor(context: Context) : this(context, null)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder {
        val itemView = inflater.inflate(viewType, parent, false)
        val holder = RecyclerViewHolder(itemView, viewType)
        innerHolderCreateListener?.apply {
            onCreateViewHolder(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        innerClickListener?.apply {
            holder.itemView.setOnClickListener {
                onClick(holder, position)
            }
        }
        innerLongClickListener?.apply {
            holder.itemView.setOnLongClickListener {
                onLongClick(holder, position)
            }
        }
        innerHolderBindListener?.apply {
            onBindViewHolder(holder, position, null)
        }
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int, payloads: MutableList<Any>) {
        innerClickListener?.apply {
            holder.itemView.setOnClickListener {
                onClick(holder, position)
            }
        }
        innerLongClickListener?.apply {
            holder.itemView.setOnLongClickListener {
                onLongClick(holder, position)
            }
        }
        innerHolderBindListener?.apply {
            onBindViewHolder(holder, position, payloads)
        }
    }
}