package com.wuhenzhizao.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.wuhenzhizao.adapter.extension.getItem
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.adapter.interfaces.*
import kotlin.reflect.KClass

/**
 * RecyclerView适配器基类
 *
 * Created by liufei on 2017/12/3.
 */
abstract class AbsRecyclerViewAdapter<T : Any, VH : RecyclerView.ViewHolder>(context: Context) : RecyclerView.Adapter<VH>() {
    val items: MutableList<T> = arrayListOf()
    var recyclerView: RecyclerView? = null
    protected val itemTypes: MutableMap<KClass<*>, ItemTypeChain> = hashMapOf()
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected var layoutInterceptor: LayoutInterceptor<T, VH>? = null
    protected var clickInterceptor: ClickInterceptor<T, VH>? = null
    protected var longClickInterceptor: LongClickInterceptor<T, VH>? = null
    protected var viewHolderCreateInterceptor: ViewHolderCreateInterceptor<T, VH>? = null
    protected var viewHolderBindInterceptor: ViewHolderBindInterceptor<T, VH>? = null

    constructor(context: Context, items: List<T>?) : this(context) {
        if (items != null) {
            putItems(items)
        }
    }

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            (getItem(position).hashCode() + position).toLong()
        } else {
            super.getItemId(position)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        val itemTypeChain = itemTypes[item::class]
        if (itemTypeChain != null) {
            return itemTypeChain.itemLayoutId
        } else if (layoutInterceptor != null) {
            return layoutInterceptor!!.run {
                getLayoutId(position, item)
            }
        }
        throw RuntimeException("Could not found the specified class")
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    open fun setInterceptor(interceptor: Interceptor<T, VH>) {
        when (interceptor) {
            is LayoutInterceptor<T, VH> -> layoutInterceptor = interceptor
            is ViewHolderCreateInterceptor<T, VH> -> viewHolderCreateInterceptor = interceptor
            is ViewHolderBindInterceptor<T, VH> -> viewHolderBindInterceptor = interceptor
            is ClickInterceptor<T, VH> -> clickInterceptor = interceptor
            is LongClickInterceptor<T, VH> -> longClickInterceptor = interceptor
        }
    }

    inline fun <reified KC> match(itemLayoutId: Int): AbsRecyclerViewAdapter<T, VH> {
        itemTypes.put(KC::class, ItemTypeChain(KC::class, itemLayoutId))
        return this
    }

    inline fun layoutInterceptor(crossinline block: (position: Int, item: T) -> Int): AbsRecyclerViewAdapter<T, VH> {
        setInterceptor(object : LayoutInterceptor<T, VH> {
            override fun getLayoutId(position: Int, item: T): Int = block.invoke(position, item)
        })
        return this
    }

    inline fun clickInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): AbsRecyclerViewAdapter<T, VH> {
        setInterceptor(object : ClickInterceptor<T, VH> {
            override fun onClick(position: Int, item: T, vh: VH) {
                block.invoke(position, item, vh)
            }

        })
        return this
    }

    inline fun longClickInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): AbsRecyclerViewAdapter<T, VH> {
        setInterceptor(object : LongClickInterceptor<T, VH> {
            override fun onLongClick(position: Int, item: T, vh: VH) {
                block.invoke(position, item, vh)
            }

        })
        return this
    }

    inline fun viewHolderCreateInterceptor(crossinline block: (vh: VH) -> Unit): AbsRecyclerViewAdapter<T, VH> {
        setInterceptor(object : ViewHolderCreateInterceptor<T, VH> {
            override fun onCreateViewHolder(vh: VH) {
                block.invoke(vh)
            }
        })
        return this
    }

    inline fun viewHolderBindInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): AbsRecyclerViewAdapter<T, VH> {
        setInterceptor(object : ViewHolderBindInterceptor<T, VH> {
            override fun onBindViewHolder(position: Int, item: T, vh: VH) {
                block.invoke(position, item, vh)
            }
        })
        return this
    }

    open fun attach(rv: RecyclerView): AbsRecyclerViewAdapter<T, VH> {
        rv.adapter = this
        return this
    }
}