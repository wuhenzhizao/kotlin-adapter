package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import com.wuhenzhizao.adapter.interfaces.Interceptor

/**
 * Created by liufei on 2017/12/3.
 */
interface ItemDragInterceptor<in T : Any, in VH> : Interceptor<T, VH> {
    /**
     * @param from
     * @param target
     */
    fun onItemDrag(from: VH, target: VH)
}

interface ItemSwipeInterceptor<in T : Any, in VH> : Interceptor<T, VH> {
    /**
     * @param vh
     * @param direction : {@link ItemTouchHelper.Left...}
     */
    fun onItemSwipe(vh: VH, direction: Int)
}