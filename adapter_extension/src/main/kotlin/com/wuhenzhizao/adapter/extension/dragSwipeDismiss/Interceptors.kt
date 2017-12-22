package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import com.wuhenzhizao.adapter.interfaces.Interceptor

/**
 * Item 被拖拽时触发
 */
interface ItemDragInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param from 开始位置的ViewHolder
     * @param target 结束位置的ViewHolder
     */
    fun onItemDrag(from: VH, target: VH)
}

/**
 * Item 滑动时触发
 */
interface ItemSwipeInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param holder
     * @param direction : 方向{ @link ItemTouchHelper.Left...}
     */
    fun onItemSwipe(holder: VH, direction: Int)
}