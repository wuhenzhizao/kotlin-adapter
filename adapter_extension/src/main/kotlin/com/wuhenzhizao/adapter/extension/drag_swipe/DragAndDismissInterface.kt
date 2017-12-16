package com.wuhenzhizao.adapter.extension.drag_swipe

/**
 * Created by liufei on 2017/12/17.
 */
interface DragAndDismissInterface {

    fun onItemMoved(fromPosition: Int, toPosition: Int)

    fun onItemDismiss(position: Int)
}