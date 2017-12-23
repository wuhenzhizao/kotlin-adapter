package com.wuhenzhizao.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.wuhenzhizao.adapter.extension.putItems
import com.wuhenzhizao.adapter.interfaces.*
import kotlin.reflect.KClass

/**
 * RecyclerView适配器基类
 *
 * Created by liufei on 2017/12/3.
 */
abstract class AbsRecyclerViewAdapter<T : Any, VH : RecyclerView.ViewHolder>(context: Context) : RecyclerView.Adapter<VH>() {
    internal val items: MutableList<T> = mutableListOf()
    internal val itemTypes: MutableMap<KClass<*>, ItemType> = mutableMapOf()
    protected var recyclerView: RecyclerView? = null
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected var innerClickListener: ClickListener<VH>? = null
    protected var innerLongClickListener: LongClickListener<VH>? = null
    protected var innerHolderCreateListener: ViewHolderCreateListener<VH>? = null
    protected var innerHolderBindListener: ViewHolderBindListener<VH>? = null
    private var innerLayoutFactory: LayoutFactory? = null

    constructor(context: Context, items: List<T>?) : this(context) {
        if (items != null) {
            putItems(items)
        }
    }

    open fun getItem(position: Int): T = items[position]

    override fun getItemId(position: Int): Long {
        return if (hasStableIds()) {
            (getItem(position).hashCode() + position).toLong()
        } else {
            super.getItemId(position)
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

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    open fun setListener(listener: Listener<VH>) {
        when (listener) {
            is ViewHolderCreateListener<VH> -> innerHolderCreateListener = listener
            is ViewHolderBindListener<VH> -> innerHolderBindListener = listener
            is ClickListener<VH> -> innerClickListener = listener
            is LongClickListener<VH> -> innerLongClickListener = listener
        }
    }

    open fun setFactory(factory: Factory) {
        when (factory) {
            is LayoutFactory -> innerLayoutFactory = factory
        }
    }
}

/**
 * 绑定适配器
 */
fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>, RV : RecyclerView> Adapter.attach(rv: RV): Adapter {
    rv.adapter = this
    return this
}

/**
 * 建立数据类与布局文件之间的匹配关系
 */
fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.match(kClass: KClass<*>, itemLayoutId: Int): Adapter {
    itemTypes.put(kClass, ItemType(itemLayoutId))
    return this
}


/**
 * 建立数据类与布局文件之间的匹配关系，当列表布局有多种样式时，可以用来代替Adapter.match()
 */
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>>
        Adapter.layoutFactory(crossinline block: (position: Int) -> Int): Adapter {
    setFactory(object : LayoutFactory {
        override fun getLayoutId(position: Int): Int = block(position)
    })
    return this
}

/**
 * 监听item layout单击事件
 */
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>>
        Adapter.clickListener(crossinline block: (holder: VH, position: Int) -> Unit): Adapter {
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
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>>
        Adapter.longClickListener(crossinline block: (holder: VH, position: Int) -> Unit): Adapter {
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
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>>
        Adapter.holderCreateListener(crossinline block: (holder: VH) -> Unit): Adapter {
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
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>>
        Adapter.holderBindListener(crossinline block: (holder: VH, position: Int) -> Unit): Adapter {
    setListener(object : ViewHolderBindListener<VH> {
        override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>?) {
            block(holder, position)
        }
    })
    return this
}

inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>>
        Adapter.holderBindListener(crossinline block: (holder: VH, position: Int, payloads: MutableList<Any>?) -> Unit): Adapter {
    setListener(object : ViewHolderBindListener<VH> {
        override fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>?) {
            block(holder, position, payloads)
        }
    })
    return this
}