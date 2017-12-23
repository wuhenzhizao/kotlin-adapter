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
    private var innerDragListener: ItemDragListener<RecyclerViewHolder>? = null
    private var innerSwipeListener: ItemSwipeListener<RecyclerViewHolder>? = null

    constructor(context: Context) : this(context, null)

    override fun onItemDrag(from: RecyclerViewHolder, target: RecyclerViewHolder) {
        val fromPosition = from.adapterPosition
        val targetPosition = target.adapterPosition
        Collections.swap(getItems(), fromPosition, targetPosition)
        super.notifyItemMoved(fromPosition, targetPosition)
        innerDragListener?.apply {
            onItemDrag(from, target)
        }
    }

    override fun onItemSwipe(holder: RecyclerViewHolder, direction: Int) {
        removeItemAt(holder.adapterPosition)
        innerSwipeListener?.apply {
            onItemSwipe(holder, direction)
        }
    }

    override fun setListener(listener: Listener<RecyclerViewHolder>) {
        super.setListener(listener)
        when (listener) {
            is ItemDragListener<RecyclerViewHolder> -> innerDragListener = listener
            is ItemSwipeListener<RecyclerViewHolder> -> innerSwipeListener = listener
        }
    }
}

/**
 * 监听recyclerView item drag & drop 操作
 */
inline fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.dragInterceptor(crossinline block: (from: RecyclerViewHolder, target: RecyclerViewHolder) -> Unit): Adapter {
    setListener(object : ItemDragListener<RecyclerViewHolder> {
        override fun onItemDrag(from: RecyclerViewHolder, target: RecyclerViewHolder) {
            block(from, target)
        }
    })
    return this
}

/**
 * 监听recyclerView item swipe dismiss 操作
 */
inline fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.swipeInterceptor(crossinline block: (viewHolder: RecyclerViewHolder, direction: Int) -> Unit): Adapter {
    setListener(object : ItemSwipeListener<RecyclerViewHolder> {
        override fun onItemSwipe(holder: RecyclerViewHolder, direction: Int) {
            block(holder, direction)
        }
    })
    return this
}