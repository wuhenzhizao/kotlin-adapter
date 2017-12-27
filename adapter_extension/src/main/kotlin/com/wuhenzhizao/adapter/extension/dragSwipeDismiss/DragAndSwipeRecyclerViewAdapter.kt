package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import android.content.Context
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.getItems
import com.wuhenzhizao.adapter.extension.removeItemAt
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder
import java.util.*

/**
 * Created by liufei on 2017/12/4.
 */
class DragAndSwipeRecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), DragAndDismissInterface<RecyclerViewHolder> {
    internal var innerDragListener: ItemDragListener? = null
    internal var innerSwipeListener: ItemSwipeListener? = null

    constructor(context: Context) : this(context, null)

    override fun onItemDrag(from: RecyclerViewHolder, target: RecyclerViewHolder) {
        val fromPosition = from.adapterPosition
        val targetPosition = target.adapterPosition
        Collections.swap(getItems(), fromPosition, targetPosition)
        super.notifyItemMoved(fromPosition, targetPosition)
        innerDragListener?.apply {
            onItemDrag(fromPosition, targetPosition)
        }
    }

    override fun onItemSwipe(holder: RecyclerViewHolder, direction: Int) {
        removeItemAt(holder.adapterPosition)
        val position = holder.position
        innerSwipeListener?.apply {
            onItemSwipe(position, direction)
        }
    }
}

/**
 * 监听recyclerView item drag & drop 操作
 */
fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.dragListener(block: (from: Int, target: Int) -> Unit): Adapter {
    innerDragListener = object : ItemDragListener {
        override fun onItemDrag(from: Int, target: Int) {
            block(from, target)
        }
    }
    return this
}

/**
 * 监听recyclerView item swipe dismiss 操作
 */
fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.swipeListener(block: (position: Int, direction: Int) -> Unit): Adapter {
    innerSwipeListener = object : ItemSwipeListener {
        override fun onItemSwipe(position: Int, direction: Int) {
            block(position, direction)
        }
    }
    return this
}