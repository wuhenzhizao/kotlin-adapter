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
    private var innerDragInterceptor: ItemDragInterceptor<T, RecyclerViewHolder>? = null
    private var innerSwipeInterceptor: ItemSwipeInterceptor<T, RecyclerViewHolder>? = null

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

    override fun onItemSwipe(vh: RecyclerViewHolder, direction: Int) {
        removeItemAt(vh.adapterPosition)
        innerSwipeInterceptor?.apply {
            onItemSwipe(vh, direction)
        }
    }

    override fun setInterceptor(interceptor: Interceptor<T, RecyclerViewHolder>) {
        super.setInterceptor(interceptor)
        when (interceptor) {
            is ItemDragInterceptor<T, RecyclerViewHolder> -> innerDragInterceptor = interceptor
            is ItemSwipeInterceptor<T, RecyclerViewHolder> -> innerSwipeInterceptor = interceptor
        }
    }
}

inline fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.dragInterceptor(crossinline block: (from: RecyclerViewHolder, target: RecyclerViewHolder) -> Unit): Adapter {
    setInterceptor(object : ItemDragInterceptor<T, RecyclerViewHolder> {
        override fun onItemDrag(from: RecyclerViewHolder, target: RecyclerViewHolder) {
            block.invoke(from, target)
        }
    })
    return this
}

inline fun <T : Any, Adapter : DragAndSwipeRecyclerViewAdapter<T>> Adapter.swipeInterceptor(crossinline block: (viewHolder: RecyclerViewHolder, direction: Int) -> Unit): Adapter {
    setInterceptor(object : ItemSwipeInterceptor<T, RecyclerViewHolder> {
        override fun onItemSwipe(vh: RecyclerViewHolder, direction: Int) {
            block.invoke(vh, direction)
        }
    })
    return this
}