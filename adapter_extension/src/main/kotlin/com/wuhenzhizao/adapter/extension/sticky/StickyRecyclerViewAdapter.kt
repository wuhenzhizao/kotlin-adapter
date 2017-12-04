package com.wuhenzhizao.adapter.extension.sticky

import android.content.Context
import android.view.ViewGroup
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder

/**
 * Created by liufei on 2017/12/4.
 */
class StickyRecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), StickyHeaderAdapter<RecyclerViewHolder> {

    constructor(context: Context) : this(context, null)

    override fun getHeaderId(position: Int): Long {
        return 0
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder? {
        return null
    }

    override fun onBindHeaderViewHolder(viewholder: RecyclerViewHolder, position: Int) {

    }
}