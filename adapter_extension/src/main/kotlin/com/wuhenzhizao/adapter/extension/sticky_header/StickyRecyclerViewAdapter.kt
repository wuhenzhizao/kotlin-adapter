package com.wuhenzhizao.adapter.extension.sticky_header

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.wuhenzhizao.adapter.AbsRecyclerViewAdapter
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder

/**
 * Created by liufei on 2017/12/4.
 */
class StickyRecyclerViewAdapter<T : StickyBean>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), StickyAdapterInterface<RecyclerViewHolder> {

    constructor(context: Context) : this(context, null)

    fun getItem(position: Int): T = items[position]

    override fun getHeaderId(position: Int): Long {
        return getItem(position).stickyId
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder? {
        val viewType = getItemViewType(position)
        val itemView = inflater.inflate(viewType, parent, false)
        val holder = RecyclerViewHolder(itemView)
        viewHolderCreateInterceptor!!.apply {
            onCreateViewHolder(holder)
        }
        return holder
    }

    override fun onBindHeaderViewHolder(viewholder: RecyclerViewHolder, position: Int) {
        clickInterceptor!!.apply {
            viewholder.itemView.setOnClickListener {
                onClick(position, getItem(position), viewholder)
            }
        }
        longClickInterceptor!!.apply {
            viewholder.itemView.setOnClickListener {
                onLongClick(position, getItem(position), viewholder)
            }
        }
        viewHolderBindInterceptor!!.apply {
            onBindViewHolder(position, getItem(position), viewholder)
        }
    }

    override fun attach(rv: RecyclerView): AbsRecyclerViewAdapter<T, RecyclerViewHolder> {
        rv.addItemDecoration(StickyRecyclerItemDecoration(this))
        return super.attach(rv)
    }
}