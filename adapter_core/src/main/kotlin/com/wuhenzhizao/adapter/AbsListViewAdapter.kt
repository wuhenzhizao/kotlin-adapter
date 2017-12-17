package com.wuhenzhizao.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.AbsListView
import android.widget.BaseAdapter
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.adapter.interfaces.*
import kotlin.reflect.KClass

/**
 * Created by liufei on 2017/12/4.
 */
abstract class AbsListViewAdapter<T : Any, VH>(context: Context) : BaseAdapter() {
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

    abstract fun onCreateViewHolder(holder: VH)

    abstract fun onBindViewHolder(position: Int, item: T, vh: VH)

    override fun getItem(position: Int): T = items[position]

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            (getItem(position).hashCode() + position).toLong()
        } else {
            position.toLong()
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

    fun setInterceptor(interceptor: Interceptor<T, VH>) {
        when (interceptor) {
            is LayoutInterceptor<T, VH> -> innerLayoutInterceptor = interceptor
            is ViewHolderCreateInterceptor<T, VH> -> innerHolderCreateInterceptor = interceptor
            is ViewHolderBindInterceptor<T, VH> -> innerHolderBindInterceptor = interceptor
            is ClickInterceptor<T, VH> -> innerClickInterceptor = interceptor
            is LongClickInterceptor<T, VH> -> innerLongClickInterceptor = interceptor
        }
    }
}

fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.attach(absListView: AbsListView): Adapter {
    absListView.adapter = this
    return this
}

fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.match(kClass: KClass<*>, itemLayoutId: Int): Adapter {
    itemTypes.put(kClass, ItemTypeChain(kClass, itemLayoutId))
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.layoutInterceptor(crossinline block: (position: Int, item: T) -> Int): Adapter {
    setInterceptor(object : LayoutInterceptor<T, VH> {
        override fun getLayoutId(position: Int, item: T): Int = block.invoke(position, item)
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.clickInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): Adapter {
    setInterceptor(object : ClickInterceptor<T, VH> {
        override fun onClick(position: Int, item: T, vh: VH) {
            block.invoke(position, item, vh)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.longClickInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): Adapter {
    setInterceptor(object : LongClickInterceptor<T, VH> {
        override fun onLongClick(position: Int, item: T, vh: VH) {
            block.invoke(position, item, vh)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.holderCreateInterceptor(crossinline block: (vh: VH) -> Unit): Adapter {
    setInterceptor(object : ViewHolderCreateInterceptor<T, VH> {
        override fun onCreateViewHolder(vh: VH) {
            block.invoke(vh)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.holderBindInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): Adapter {
    setInterceptor(object : ViewHolderBindInterceptor<T, VH> {
        override fun onBindViewHolder(position: Int, item: T, vh: VH) {
            block.invoke(position, item, vh)
        }
    })
    return this
}