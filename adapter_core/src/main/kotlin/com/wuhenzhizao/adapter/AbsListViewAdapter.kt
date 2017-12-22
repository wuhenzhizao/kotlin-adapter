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
    val items: MutableList<T> = mutableListOf()
    val itemTypes: MutableMap<KClass<*>, ItemType> = hashMapOf()
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected var innerLayoutInterceptor: LayoutInterceptor<VH>? = null
    protected var innerClickInterceptor: ClickInterceptor<VH>? = null
    protected var innerLongClickInterceptor: LongClickInterceptor<VH>? = null
    protected var innerHolderCreateInterceptor: ViewHolderCreateInterceptor<VH>? = null
    protected var innerHolderBindInterceptor: ViewHolderBindInterceptor<VH>? = null

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
        val itemType = itemTypes[item::class]
        if (itemType != null) {
            return itemType.itemLayoutId
        } else if (innerLayoutInterceptor != null) {
            return innerLayoutInterceptor!!.run {
                getLayoutId(position)
            }
        }
        throw RuntimeException("Could not found the specified class")
    }

    fun setInterceptor(interceptor: Interceptor<VH>) {
        when (interceptor) {
            is LayoutInterceptor<VH> -> innerLayoutInterceptor = interceptor
            is ViewHolderCreateInterceptor<VH> -> innerHolderCreateInterceptor = interceptor
            is ViewHolderBindInterceptor<VH> -> innerHolderBindInterceptor = interceptor
            is ClickInterceptor<VH> -> innerClickInterceptor = interceptor
            is LongClickInterceptor<VH> -> innerLongClickInterceptor = interceptor
        }
    }
}

fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.attach(absListView: AbsListView): Adapter {
    absListView.adapter = this
    return this
}

fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.match(kClass: KClass<*>, itemLayoutId: Int): Adapter {
    itemTypes.put(kClass, ItemType(kClass, itemLayoutId))
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.layoutInterceptor(crossinline block: (position: Int) -> Int): Adapter {
    setInterceptor(object : LayoutInterceptor<VH> {
        override fun getLayoutId(position: Int): Int = block(position)
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.clickInterceptor(crossinline block: (position: Int, holder: VH) -> Unit): Adapter {
    setInterceptor(object : ClickInterceptor<VH> {
        override fun onClick(position: Int, holder: VH) {
            block(position, holder)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.longClickInterceptor(crossinline block: (position: Int, holder: VH) -> Unit): Adapter {
    setInterceptor(object : LongClickInterceptor<VH> {
        override fun onLongClick(position: Int, holder: VH): Boolean {
            block(position, holder)
            return false
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.holderCreateInterceptor(crossinline block: (holder: VH) -> Unit): Adapter {
    setInterceptor(object : ViewHolderCreateInterceptor<VH> {
        override fun onCreateViewHolder(holder: VH) {
            block(holder)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.holderBindInterceptor(crossinline block: (position: Int, holder: VH) -> Unit): Adapter {
    setInterceptor(object : ViewHolderBindInterceptor<VH> {
        override fun onBindViewHolder(position: Int, holder: VH) {
            block(position, holder)
        }
    })
    return this
}