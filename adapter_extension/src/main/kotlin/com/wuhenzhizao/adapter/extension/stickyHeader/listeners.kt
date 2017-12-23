package com.wuhenzhizao.adapter.extension.stickyHeader

import android.view.View
import com.wuhenzhizao.adapter.interfaces.Listener

/**
 * 监听Sticky header item view的创建
 */
interface HeaderViewHolderCreateListener<in VH> : Listener<VH> {
    /**
     * @param holder
     */
    fun onCreateHeaderViewHolder(holder: VH)
}

/**
 * 监听Sticky header item view的绑定
 */
interface HeaderViewHolderBindListener<in VH> : Listener<VH> {
    /**
     * @param holder
     * @param position
     */
    fun onBindHeaderViewHolder(holder: VH, position: Int)
}

/**
 * 监听Sticky header item view的点击事件
 */
interface HeaderClickListener<in VH> : Listener<VH> {
    /**
     * @param holder
     * @param clickView
     * @param position
     */
    fun onHeaderClick(holder: VH, clickView: View, position: Int)
}