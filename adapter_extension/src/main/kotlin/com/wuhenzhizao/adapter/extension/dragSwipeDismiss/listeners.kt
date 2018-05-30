package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import com.wuhenzhizao.adapter.interfaces.Listener

/**
 * Item 被拖拽时触发
 */
interface ItemDragListener<in VH> : Listener<VH> {
    /**
     * @param from 开始位置
     * @param target 结束位置
     */
    fun onItemDrag(from: Int, target: Int)
}

/**
 * Item 滑动时触发
 */
interface ItemSwipeListener<in VH> : Listener<VH> {
    /**
     * @param position
     * @param direction : 方向{ @link ItemTouchHelper.Left...}
     */
    fun onItemSwipe(position: Int, direction: Int)
}