package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

/**
 * Item 被拖拽时触发
 */
interface ItemDragListener {
    /**
     * @param from 开始位置
     * @param target 结束位置
     */
    fun onItemDrag(from: Int, target: Int)
}

/**
 * Item 滑动时触发
 */
interface ItemSwipeListener {
    /**
     * @param position
     * @param direction : 方向{ @link ItemTouchHelper.Left...}
     */
    fun onItemSwipe(position: Int, direction: Int)
}