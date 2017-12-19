package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

/**
 * Created by liufei on 2017/12/17.
 */
interface DragAndDismissInterface<in VH> {

    fun onItemDrag(from: VH, target: VH)

    fun onItemSwipe(holder: VH, direction: Int)
}