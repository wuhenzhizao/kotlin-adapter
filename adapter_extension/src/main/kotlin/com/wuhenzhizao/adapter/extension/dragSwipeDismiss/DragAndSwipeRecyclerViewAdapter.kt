package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import android.content.Context
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.getItems
import com.wuhenzhizao.adapter.extension.removeItemAt
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder
import com.wuhenzhizao.adapter.interfaces.Listener
import java.util.*

/**
 * Created by liufei on 2017/12/4.
 */
class DragAndSwipeRecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), DragAndDismissInterface<RecyclerViewHolder> {
    internal var innerDragListener: ItemDragListener<RecyclerViewHolder>? = null
    internal var innerSwipeListener: ItemSwipeListener<RecyclerViewHolder>? = null

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

    override fun setListener(listener: Listener<RecyclerViewHolder>) {
        super.setListener(listener)
        when (listener) {
            is ItemDragListener -> innerDragListener = listener
            is ItemSwipeListener -> innerSwipeListener = listener
        }
    }
}

/**
 * 监听recyclerView item drag & drop 操作
 */
inline fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.dragListener(crossinline block: (from: Int, target: Int) -> Unit): Adapter {
    setListener(object : ItemDragListener<RecyclerViewHolder> {
        override fun onItemDrag(from: Int, target: Int) {
            block(from, target)
        }
    })
    return this
}

/**
 * 监听recyclerView item swipe dismiss 操作
 */
inline fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.swipeListener(crossinline block: (position: Int, direction: Int) -> Unit): Adapter {
    setListener(object : ItemSwipeListener<RecyclerViewHolder> {
        override fun onItemSwipe(position: Int, direction: Int) {
            block(position, direction)
        }
    })
    return this
}