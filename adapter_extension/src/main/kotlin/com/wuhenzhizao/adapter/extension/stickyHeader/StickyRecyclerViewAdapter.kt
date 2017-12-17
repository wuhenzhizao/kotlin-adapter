package com.wuhenzhizao.adapter.extension.stickyHeader

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.wuhenzhizao.adapter.AbsRecyclerViewAdapter
import com.wuhenzhizao.adapter.ItemTypeChain
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder
import com.wuhenzhizao.adapter.interfaces.Interceptor
import kotlin.reflect.KClass

/**
 * Created by liufei on 2017/12/4.
 */
class StickyRecyclerViewAdapter<T : StickyBean>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), StickyAdapterInterface<RecyclerViewHolder> {
    val headerTypes: MutableMap<KClass<*>, ItemTypeChain> = hashMapOf()
    internal var innerHeaderClickInterceptor: HeaderClickInterceptor<T, RecyclerViewHolder>? = null
    private var innerHeaderHolderCreateInterceptor: HeaderViewHolderCreateInterceptor<T, RecyclerViewHolder>? = null
    private var innerHeaderHolderBindInterceptor: HeaderViewHolderBindInterceptor<T, RecyclerViewHolder>? = null

    constructor(context: Context) : this(context, null)

    override fun getHeaderId(position: Int): Long {
        return getItem(position).stickyId
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder? {
        val item = getItem(position)
        val viewType = headerTypes[item::class]!!.itemLayoutId
        val itemView = inflater.inflate(viewType, parent, false)
        val holder = RecyclerViewHolder(itemView)
        holder.layoutId = viewType
        innerHeaderHolderCreateInterceptor?.apply {
            onCreateHeaderViewHolder(holder)
        }
        return holder
    }

    override fun onBindHeaderViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        innerHeaderHolderBindInterceptor?.apply {
            onBindHeaderViewHolder(position, getItem(position), viewHolder)
        }
    }

    override fun setInterceptor(interceptor: Interceptor<T, RecyclerViewHolder>) {
        super.setInterceptor(interceptor)
        when (interceptor) {
            is HeaderClickInterceptor<T, RecyclerViewHolder> -> innerHeaderClickInterceptor = interceptor
            is HeaderViewHolderCreateInterceptor<T, RecyclerViewHolder> -> innerHeaderHolderCreateInterceptor = interceptor
            is HeaderViewHolderBindInterceptor<T, RecyclerViewHolder> -> innerHeaderHolderBindInterceptor = interceptor
        }
    }
}

fun <T : Any, Adapter : StickyRecyclerViewAdapter<T>> Adapter.attach(rv: RecyclerView): Adapter {
    rv.addItemDecoration(StickyRecyclerItemDecoration(this))
    rv.adapter = this
    return this
}

fun <T : Any, Adapter : StickyRecyclerViewAdapter<T>> Adapter.matchHeader(kClass: KClass<T>, itemLayoutId: Int): Adapter {
    headerTypes.put(kClass, ItemTypeChain(kClass, itemLayoutId))
    return this
}

inline fun <T : Any, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerClickInterceptor(crossinline block: (position: Int, stickyId: Long) -> Unit): Adapter {
    setInterceptor(object : HeaderClickInterceptor<T, RecyclerViewHolder> {
        override fun onHeaderClick(position: Int, stickyId: Long) {
            block.invoke(position, stickyId)
        }
    })
    return this
}

inline fun <T : Any, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerHolderCreateInterceptor(crossinline block: (viewHolder: RecyclerViewHolder) -> Unit): Adapter {
    setInterceptor(object : HeaderViewHolderCreateInterceptor<T, RecyclerViewHolder> {
        override fun onCreateHeaderViewHolder(vh: RecyclerViewHolder) {
            block.invoke(vh)
        }
    })
    return this
}

inline fun <T : Any, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerHolderBindInterceptor(crossinline block: (position: Int, item: T, viewHolder: RecyclerViewHolder) -> Unit): Adapter {
    setInterceptor(object : HeaderViewHolderBindInterceptor<T, RecyclerViewHolder> {
        override fun onBindHeaderViewHolder(position: Int, item: T, vh: RecyclerViewHolder) {
            block.invoke(position, item, vh)
        }
    })
    return this
}