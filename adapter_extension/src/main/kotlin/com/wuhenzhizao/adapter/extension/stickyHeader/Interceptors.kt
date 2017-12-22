package com.wuhenzhizao.adapter.extension.stickyHeader

import android.view.View
import com.wuhenzhizao.adapter.interfaces.Interceptor

/**
 * 监听Sticky header item view的创建
 */
interface HeaderViewHolderCreateInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param holder
     */
    fun onCreateHeaderViewHolder(holder: VH)
}

/**
 * 监听Sticky header item view的绑定
 */
interface HeaderViewHolderBindInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position
     * @param holder
     */
    fun onBindHeaderViewHolder(position: Int, holder: VH)
}

/**
 * 监听Sticky header item view的点击事件
 */
interface HeaderClickInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param holder
     * @param clickView
     * @param position
     */
    fun onHeaderClick(holder: VH, clickView: View, position: Int)
}