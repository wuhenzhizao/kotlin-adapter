package com.wuhenzhizao.adapter.extension.drag_swipe

import android.content.Context
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.removeItemAt
import java.util.*

/**
 * Created by liufei on 2017/12/4.
 */
class DragAndSwipeRecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items) {

    fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        super.notifyItemMoved(fromPosition, toPosition)
    }

    fun onItemDismiss(position: Int) {
        removeItemAt(position)
    }
}