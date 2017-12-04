package com.wuhenzhizao.adapter.extension.sticky

import android.view.ViewGroup

/**
 * Created by liufei on 2017/12/4.
 */
interface StickyHeaderAdapter<VH> {

    fun getHeaderId(position: Int): Long


    fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): VH?


    fun onBindHeaderViewHolder(viewholder: VH, position: Int)
}