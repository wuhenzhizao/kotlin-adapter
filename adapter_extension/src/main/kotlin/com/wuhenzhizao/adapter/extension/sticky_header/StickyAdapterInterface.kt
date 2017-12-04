package com.wuhenzhizao.adapter.extension.sticky_header

import android.view.ViewGroup

/**
 * Created by liufei on 2017/12/4.
 */
interface StickyAdapterInterface<VH> {

    fun getHeaderId(position: Int): Long


    fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): VH?


    fun onBindHeaderViewHolder(viewholder: VH, position: Int)
}