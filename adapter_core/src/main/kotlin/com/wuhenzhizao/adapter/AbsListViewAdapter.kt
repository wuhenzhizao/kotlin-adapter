package com.wuhenzhizao.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.AbsListView
import android.widget.BaseAdapter
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.adapter.interfaces.*
import kotlin.reflect.KClass

/**
 * AbsListView Adapter基类
 *
 * Created by liufei on 2017/12/4.
 */
abstract class AbsListViewAdapter<T : Any, VH>(context: Context) : BaseAdapter() {
    internal var items: MutableList<T> = mutableListOf()
    internal var itemTypes: MutableMap<KClass<*>, ItemType> = mutableMapOf()
    internal var innerClickListener: ClickListener<VH>? = null
    internal var innerLongClickListener: LongClickListener<VH>? = null
    internal var innerHolderCreateListener: ViewHolderCreateListener<VH>? = null
    internal var innerHolderBindListener: ViewHolderBindListener<VH>? = null
    internal var innerLayoutFactory: LayoutFactory? = null
    protected val inflater: LayoutInflater = LayoutInflater.from(context)

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
        } else if (innerLayoutFactory != null) {
            return innerLayoutFactory!!.run {
                getLayoutId(position)
            }
        }
        throw RuntimeException("Could not found the specified class")
    }

    open fun setListener(listener: Listener<VH>) {
        when (listener) {
            is ViewHolderCreateListener<VH> -> innerHolderCreateListener = listener
            is ViewHolderBindListener<VH> -> innerHolderBindListener = listener
            is ClickListener<VH> -> innerClickListener = listener
            is LongClickListener<VH> -> innerLongClickListener = listener
        }
    }

    fun setFactory(factory: Factory) {
        when (factory) {
            is LayoutFactory -> innerLayoutFactory = factory
        }
    }
}

/**
 * 绑定适配器
 */
fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.attach(absListView: AbsListView): Adapter {
    absListView.adapter = this
    return this
}

/**
 * 建立数据类与布局文件之间的匹配关系
 */
fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.match(kClass: KClass<*>, itemLayoutId: Int): Adapter {
    itemTypes.put(kClass, ItemType(itemLayoutId))
    return this
}

/**
 * 建立数据类与布局文件之间的匹配关系，当列表布局有多种样式时，可以用来代替Adapter.match()
 */
inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.layoutFactory(crossinline block: (position: Int) -> Int): Adapter {
    setFactory(object : LayoutFactory {
        override fun getLayoutId(position: Int): Int = block(position)
    })
    return this
}

/**
 * 监听item layout单击事件
 */
inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.clickListener(crossinline block: (holder: VH, position: Int) -> Unit): Adapter {
    setListener(object : ClickListener<VH> {
        override fun onClick(holder: VH, position: Int) {
            block(holder, position)
        }
    })
    return this
}

/**
 * 监听item layout长按事件
 */
inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.longClickListener(crossinline block: (holder: VH, position: Int) -> Unit): Adapter {
    setListener(object : LongClickListener<VH> {
        override fun onLongClick(holder: VH, position: Int): Boolean {
            block(holder, position)
            return false
        }
    })
    return this
}

/**
 * View Holder创建时触发
 */
inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.holderCreateListener(crossinline block: (holder: VH) -> Unit): Adapter {
    setListener(object : ViewHolderCreateListener<VH> {
        override fun onCreateViewHolder(holder: VH) {
            block(holder)
        }
    })
    return this
}

/**
 * View Holder绑定时触发
 */
inline fun <T : Any, VH, Adapter : AbsListViewAdapter<T, VH>> Adapter.holderBindListener(crossinline block: (holder: VH, position: Int) -> Unit): Adapter {
    setListener(object : ViewHolderBindListener<VH> {
        override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>?) {
            block(holder, position)
        }
    })
    return this
}