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
    var recyclerView: RecyclerView? = null
    val items: MutableList<T> = arrayListOf()
    val itemTypes: MutableMap<KClass<*>, ItemTypeChain> = hashMapOf()
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected var innerLayoutInterceptor: LayoutInterceptor<T, VH>? = null
    protected var innerClickInterceptor: ClickInterceptor<T, VH>? = null
    protected var innerLongClickInterceptor: LongClickInterceptor<T, VH>? = null
    protected var innerHolderCreateInterceptor: ViewHolderCreateInterceptor<T, VH>? = null
    protected var innerHolderBindInterceptor: ViewHolderBindInterceptor<T, VH>? = null

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
        } else if (innerLayoutInterceptor != null) {
            return innerLayoutInterceptor!!.run {
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
            is LayoutInterceptor<T, VH> -> innerLayoutInterceptor = interceptor
            is ViewHolderCreateInterceptor<T, VH> -> innerHolderCreateInterceptor = interceptor
            is ViewHolderBindInterceptor<T, VH> -> innerHolderBindInterceptor = interceptor
            is ClickInterceptor<T, VH> -> innerClickInterceptor = interceptor
            is LongClickInterceptor<T, VH> -> innerLongClickInterceptor = interceptor
        }
    }

    open fun attach(rv: RecyclerView): AbsRecyclerViewAdapter<T, VH> {
        rv.adapter = this
        return this
    }
}

fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.match(kClass: KClass<*>, itemLayoutId: Int): Adapter {
    itemTypes.put(kClass, ItemTypeChain(kClass, itemLayoutId))
    return this
}

inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.layoutInterceptor(crossinline block: (position: Int, item: T) -> Int): Adapter {
    setInterceptor(object : LayoutInterceptor<T, VH> {
        override fun getLayoutId(position: Int, item: T): Int = block.invoke(position, item)
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.clickInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): Adapter {
    setInterceptor(object : ClickInterceptor<T, VH> {
        override fun onClick(position: Int, item: T, vh: VH) {
            block.invoke(position, item, vh)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.longClickInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): Adapter {
    setInterceptor(object : LongClickInterceptor<T, VH> {
        override fun onLongClick(position: Int, item: T, vh: VH) {
            block.invoke(position, item, vh)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.holderCreateInterceptor(crossinline block: (vh: VH) -> Unit): Adapter {
    setInterceptor(object : ViewHolderCreateInterceptor<T, VH> {
        override fun onCreateViewHolder(vh: VH) {
            block.invoke(vh)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.holderBindInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): Adapter {
    setInterceptor(object : ViewHolderBindInterceptor<T, VH> {
        override fun onBindViewHolder(position: Int, item: T, vh: VH) {
            block.invoke(position, item, vh)
        }
    })
    return this
}