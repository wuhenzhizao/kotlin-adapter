package com.wuhenzhizao.adapter.extension.stickyHeader

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.wuhenzhizao.adapter.ItemTypeChain
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder
import com.wuhenzhizao.adapter.interfaces.Interceptor
import kotlin.reflect.KClass

/**
 * Created by liufei on 2017/12/4.
 */
open class StickyRecyclerViewAdapter<T : StickyBean>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), StickyAdapterInterface<RecyclerViewHolder> {
    val headerTypes: MutableMap<KClass<*>, ItemTypeChain> = hashMapOf()
    internal var innerHeaderClickInterceptor: HeaderClickInterceptor<RecyclerViewHolder>? = null
    private var innerHeaderHolderCreateInterceptor: HeaderViewHolderCreateInterceptor<RecyclerViewHolder>? = null
    private var innerHeaderHolderBindInterceptor: HeaderViewHolderBindInterceptor<RecyclerViewHolder>? = null

    constructor(context: Context) : this(context, null)

    override fun getHeaderId(position: Int): Long {
        return getItem(position).stickyId
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder? {
        val item = getItem(position)
        if (position == 23) {
            println(position)
        }
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
            onBindHeaderViewHolder(position, viewHolder)
        }
    }

    override fun setInterceptor(interceptor: Interceptor<RecyclerViewHolder>) {
        super.setInterceptor(interceptor)
        when (interceptor) {
            is HeaderClickInterceptor<RecyclerViewHolder> -> innerHeaderClickInterceptor = interceptor
            is HeaderViewHolderCreateInterceptor<RecyclerViewHolder> -> innerHeaderHolderCreateInterceptor = interceptor
            is HeaderViewHolderBindInterceptor<RecyclerViewHolder> -> innerHeaderHolderBindInterceptor = interceptor
        }
    }
}

fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.attach(rv: RecyclerView): Adapter {
    val stickyDecoration = StickyRecyclerItemDecoration(this)
    rv.addItemDecoration(stickyDecoration)
    rv.adapter = this
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            stickyDecoration.clearHeaderCache()
        }
    })
    return this
}

fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.matchHeader(kClass: KClass<*>, itemLayoutId: Int): Adapter {
    headerTypes.put(kClass, ItemTypeChain(kClass, itemLayoutId))
    return this
}

inline fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerClickInterceptor(crossinline block: (position: Int, stickyId: Long) -> Unit): Adapter {
    setInterceptor(object : HeaderClickInterceptor<RecyclerViewHolder> {
        override fun onHeaderClick(position: Int, stickyId: Long) {
            block.invoke(position, stickyId)
        }
    })
    return this
}

inline fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerHolderCreateInterceptor(crossinline block: (viewHolder: RecyclerViewHolder) -> Unit): Adapter {
    setInterceptor(object : HeaderViewHolderCreateInterceptor<RecyclerViewHolder> {
        override fun onCreateHeaderViewHolder(vh: RecyclerViewHolder) {
            block.invoke(vh)
        }
    })
    return this
}

inline fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerHolderBindInterceptor(crossinline block: (position: Int, viewHolder: RecyclerViewHolder) -> Unit): Adapter {
    setInterceptor(object : HeaderViewHolderBindInterceptor<RecyclerViewHolder> {
        override fun onBindHeaderViewHolder(position: Int, vh: RecyclerViewHolder) {
            block.invoke(position, vh)
        }
    })
    return this
}