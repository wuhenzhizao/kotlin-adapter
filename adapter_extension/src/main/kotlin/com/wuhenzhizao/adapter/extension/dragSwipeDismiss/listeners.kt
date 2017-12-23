package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import com.wuhenzhizao.adapter.interfaces.Listener

/**
 * Item 被拖拽时触发
 */
interface ItemDragListener<in VH> : Listener<VH> {
    /**
     * @param from 开始位置的ViewHolder
     * @param target 结束位置的ViewHolder
     */
    fun onItemDrag(from: VH, target: VH)
}

/**
 * Item 滑动时触发
 */
interface ItemSwipeListener<in VH> : Listener<VH> {
    /**
     * @param holder
     * @param direction : 方向{ @link ItemTouchHelper.Left...}
     */
    fun onItemSwipe(holder: VH, direction: Int)
}