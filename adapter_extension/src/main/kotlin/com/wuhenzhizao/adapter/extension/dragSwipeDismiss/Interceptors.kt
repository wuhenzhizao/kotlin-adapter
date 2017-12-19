package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import com.wuhenzhizao.adapter.interfaces.Interceptor

/**
 * Created by liufei on 2017/12/3.
 */
interface ItemDragInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param from
     * @param target
     */
    fun onItemDrag(from: VH, target: VH)
}

interface ItemSwipeInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param holder
     * @param direction : { @link ItemTouchHelper.Left...}
     */
    fun onItemSwipe(holder: VH, direction: Int)
}