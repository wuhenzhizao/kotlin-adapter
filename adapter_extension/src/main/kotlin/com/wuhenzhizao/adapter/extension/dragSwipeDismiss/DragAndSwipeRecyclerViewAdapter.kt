package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import android.content.Context
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.removeItemAt
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder
import com.wuhenzhizao.adapter.interfaces.Interceptor
import java.util.*

/**
 * Created by liufei on 2017/12/4.
 */
class DragAndSwipeRecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), DragAndDismissInterface<RecyclerViewHolder> {
    private var innerDragInterceptor: ItemDragInterceptor<RecyclerViewHolder>? = null
    private var innerSwipeInterceptor: ItemSwipeInterceptor<RecyclerViewHolder>? = null

    constructor(context: Context) : this(context, null)

    override fun onItemDrag(from: RecyclerViewHolder, target: RecyclerViewHolder) {
        val fromPosition = from.adapterPosition
        val targetPosition = target.adapterPosition
        Collections.swap(items, fromPosition, targetPosition)
        super.notifyItemMoved(fromPosition, targetPosition)
        innerDragInterceptor?.apply {
            onItemDrag(from, target)
        }
    }

    override fun onItemSwipe(holder: RecyclerViewHolder, direction: Int) {
        removeItemAt(holder.adapterPosition)
        innerSwipeInterceptor?.apply {
            onItemSwipe(holder, direction)
        }
    }

    override fun setInterceptor(interceptor: Interceptor<RecyclerViewHolder>) {
        super.setInterceptor(interceptor)
        when (interceptor) {
            is ItemDragInterceptor<RecyclerViewHolder> -> innerDragInterceptor = interceptor
            is ItemSwipeInterceptor<RecyclerViewHolder> -> innerSwipeInterceptor = interceptor
        }
    }
}

inline fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.dragInterceptor(crossinline block: (from: RecyclerViewHolder, target: RecyclerViewHolder) -> Unit): Adapter {
    setInterceptor(object : ItemDragInterceptor<RecyclerViewHolder> {
        override fun onItemDrag(from: RecyclerViewHolder, target: RecyclerViewHolder) {
            block.invoke(from, target)
        }
    })
    return this
}

inline fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.swipeInterceptor(crossinline block: (viewHolder: RecyclerViewHolder, direction: Int) -> Unit): Adapter {
    setInterceptor(object : ItemSwipeInterceptor<RecyclerViewHolder> {
        override fun onItemSwipe(holder: RecyclerViewHolder, direction: Int) {
            block.invoke(holder, direction)
        }
    })
    return this
}