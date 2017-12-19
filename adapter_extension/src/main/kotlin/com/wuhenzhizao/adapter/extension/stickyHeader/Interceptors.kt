package com.wuhenzhizao.adapter.extension.stickyHeader

import android.view.View
import com.wuhenzhizao.adapter.interfaces.Interceptor

/**
 * Created by liufei on 2017/12/3.
 */
interface HeaderViewHolderCreateInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param holder
     */
    fun onCreateHeaderViewHolder(holder: VH)
}

interface HeaderViewHolderBindInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position
     * @param holder
     */
    fun onBindHeaderViewHolder(position: Int, holder: VH)
}


interface HeaderClickInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param holder
     * @param clickView
     * @param position
     */
    fun onHeaderClick(holder: VH, clickView: View, position: Int)
}