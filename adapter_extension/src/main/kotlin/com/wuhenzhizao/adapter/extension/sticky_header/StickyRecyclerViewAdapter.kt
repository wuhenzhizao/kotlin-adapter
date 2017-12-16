package com.wuhenzhizao.adapter.extension.sticky_header

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
    protected val headerTypes: MutableMap<KClass<*>, ItemTypeChain> = hashMapOf()
    protected var headerViewHolderCreateInterceptor: HeaderViewHolderCreateInterceptor<T, RecyclerViewHolder>? = null
    protected var headerViewHolderBindInterceptor: HeaderViewHolderBindInterceptor<T, RecyclerViewHolder>? = null

    constructor(context: Context) : this(context, null)

    fun getItem(position: Int): T = items[position]

    override fun getHeaderId(position: Int): Long {
        return getItem(position).stickyId
    }

    override fun onCreateHeaderViewHolder(parent: ViewGroup, position: Int): RecyclerViewHolder? {
        val item = getItem(position)
        val viewType = headerTypes[item::class]!!.itemLayoutId
        val itemView = inflater.inflate(viewType, parent, false)
        val holder = RecyclerViewHolder(itemView)
        holder.layoutId = viewType
        headerViewHolderCreateInterceptor?.apply {
            onCreateHeaderViewHolder(holder)
        }
        return holder
    }

    override fun onBindHeaderViewHolder(viewHolder: RecyclerViewHolder, position: Int) {
        headerViewHolderBindInterceptor?.apply {
            onBindHeaderViewHolder(position, getItem(position), viewHolder)
        }
    }

    override fun attach(rv: RecyclerView): AbsRecyclerViewAdapter<T, RecyclerViewHolder> {
        rv.addItemDecoration(StickyRecyclerItemDecoration(this))
        return super.attach(rv)
    }

    inline fun <reified KC> matchHeader(itemLayoutId: Int): StickyRecyclerViewAdapter<T> {
        headerTypes.put(KC::class, ItemTypeChain(KC::class, itemLayoutId))
        return this
    }

    override fun setInterceptor(interceptor: Interceptor<T, RecyclerViewHolder>) {
        super.setInterceptor(interceptor)
        when (interceptor) {
            is HeaderViewHolderCreateInterceptor<T, RecyclerViewHolder> -> headerViewHolderCreateInterceptor = interceptor
            is HeaderViewHolderBindInterceptor<T, RecyclerViewHolder> -> headerViewHolderBindInterceptor = interceptor
        }
    }

    inline fun headerViewHolderCreateInterceptor(crossinline block: (viewHolder: RecyclerViewHolder) -> Unit): StickyRecyclerViewAdapter<T> {
        setInterceptor(object : HeaderViewHolderCreateInterceptor<T, RecyclerViewHolder> {
            override fun onCreateHeaderViewHolder(vh: RecyclerViewHolder) {
                block.invoke(vh)
            }
        })
        return this
    }

    inline fun headerViewHolderBindInterceptor(crossinline block: (position: Int, item: T, viewHolder: RecyclerViewHolder) -> Unit): StickyRecyclerViewAdapter<T> {
        setInterceptor(object : HeaderViewHolderBindInterceptor<T, RecyclerViewHolder> {
            override fun onBindHeaderViewHolder(position: Int, item: T, vh: RecyclerViewHolder) {
                block.invoke(position, item, vh)
            }
        })
        return this
    }
}