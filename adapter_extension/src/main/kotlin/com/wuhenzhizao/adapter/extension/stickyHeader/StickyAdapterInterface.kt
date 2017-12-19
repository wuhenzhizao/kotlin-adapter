package com.wuhenzhizao.adapter.extension.stickyHeader

import android.view.ViewGroup

/**
 * Created by liufei on 2017/12/4.
 */
interface StickyAdapterInterface<VH> {

    fun getHeaderId(position: Int): Long


    fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): VH?


    fun onBindHeaderViewHolder(holder: VH, position: Int)
}