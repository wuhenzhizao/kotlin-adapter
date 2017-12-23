package com.wuhenzhizao.adapter.extension.stickyHeader

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wuhenzhizao.adapter.ItemType
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.R
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder
import com.wuhenzhizao.adapter.interfaces.Listener
import kotlin.reflect.KClass

/**
 * Created by liufei on 2017/12/4.
 */
open class StickyRecyclerViewAdapter<T : StickyBean>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), StickyAdapterInterface<RecyclerViewHolder> {
    val headerTypes: MutableMap<KClass<*>, ItemType> = mutableMapOf()
    internal var innerHeaderClickListener: HeaderClickListener<RecyclerViewHolder>? = null
    private var innerHeaderHolderCreateListener: HeaderViewHolderCreateListener<RecyclerViewHolder>? = null
    private var innerHeaderHolderBindListener: HeaderViewHolderBindListener<RecyclerViewHolder>? = null

    constructor(context: Context) : this(context, null)

    override fun getHeaderId(position: Int): Long {
        return getItem(position).stickyId
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder? {
        val item = getItem(position)
        val viewType = headerTypes[item::class]!!.itemLayoutId
        val itemView = inflater.inflate(viewType, parent, false)
        val holder = RecyclerViewHolder(itemView, viewType)
        innerHeaderHolderCreateListener?.apply {
            onCreateHeaderViewHolder(holder)
        }
        return holder
    }

    override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.itemView.setTag(R.id.sticky_position, position)
        innerHeaderHolderBindListener?.apply {
            onBindHeaderViewHolder(holder, position)
        }
    }

    override fun setListener(listener: Listener<RecyclerViewHolder>) {
        super.setListener(listener)
        when (listener) {
            is HeaderClickListener<RecyclerViewHolder> -> innerHeaderClickListener = listener
            is HeaderViewHolderCreateListener<RecyclerViewHolder> -> innerHeaderHolderCreateListener = listener
            is HeaderViewHolderBindListener<RecyclerViewHolder> -> innerHeaderHolderBindListener = listener
        }
    }
}

/**
 * 绑定适配器
 */
fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.attach(rv: RecyclerView): Adapter {
    rv.adapter = this
    val stickyDecoration = StickyRecyclerItemDecoration(this)
    rv.addItemDecoration(stickyDecoration)
    val touchHelper = StickyHeaderTouchHelper(rv, stickyDecoration)
    rv.addOnItemTouchListener(touchHelper)
    registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            stickyDecoration.clearHeaderCache()
        }
    })
    return this
}

/**
 * 建立数据类与header布局文件之间的匹配关系
 */
fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.matchHeader(kClass: KClass<*>, itemLayoutId: Int): Adapter {
    headerTypes.put(kClass, ItemType(itemLayoutId))
    return this
}

/**
 * 监听header单击事件
 */
inline fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerClickListener(crossinline block: (holder: RecyclerViewHolder, clickView: View, position: Int) -> Unit): Adapter {
    setListener(object : HeaderClickListener<RecyclerViewHolder> {
        override fun onHeaderClick(holder: RecyclerViewHolder, clickView: View, position: Int) {
            block(holder, clickView, position)
        }
    })
    return this
}

/**
 * Header view holder创建时触发
 */
inline fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerHolderCreateListener(crossinline block: (holder: RecyclerViewHolder) -> Unit): Adapter {
    setListener(object : HeaderViewHolderCreateListener<RecyclerViewHolder> {
        override fun onCreateHeaderViewHolder(holder: RecyclerViewHolder) {
            block(holder)
        }
    })
    return this
}

/**
 * Header view holder绑定时触发
 */
inline fun <T : StickyBean, Adapter : StickyRecyclerViewAdapter<T>> Adapter.headerHolderBindListener(crossinline block: (holder: RecyclerViewHolder, position: Int) -> Unit): Adapter {
    setListener(object : HeaderViewHolderBindListener<RecyclerViewHolder> {
        override fun onBindHeaderViewHolder(holder: RecyclerViewHolder, position: Int) {
            block(holder, position)
        }
    })
    return this
}