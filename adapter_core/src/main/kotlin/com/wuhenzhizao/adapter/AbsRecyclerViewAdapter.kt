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
    var recyclerView: RecyclerView? = null
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
        } else if (innerLayoutInterceptor != null) {
            return innerLayoutInterceptor!!.run {
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

    open fun setInterceptor(interceptor: Interceptor<VH>) {
        when (interceptor) {
            is LayoutInterceptor<VH> -> innerLayoutInterceptor = interceptor
            is ViewHolderCreateInterceptor<VH> -> innerHolderCreateInterceptor = interceptor
            is ViewHolderBindInterceptor<VH> -> innerHolderBindInterceptor = interceptor
            is ClickInterceptor<VH> -> innerClickInterceptor = interceptor
            is LongClickInterceptor<VH> -> innerLongClickInterceptor = interceptor
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
    itemTypes.put(kClass, ItemType(kClass, itemLayoutId))
    return this
}


/**
 * 建立数据类与布局文件之间的匹配关系，当列表布局有多种样式时，可以用来代替Adapter.match()
 */
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.layoutInterceptor(crossinline block: (position: Int) -> Int): Adapter {
    setInterceptor(object : LayoutInterceptor<VH> {
        override fun getLayoutId(position: Int): Int = block(position)
    })
    return this
}

/**
 * 监听item layout单击事件
 */
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.clickInterceptor(crossinline block: (position: Int, holder: VH) -> Unit): Adapter {
    setInterceptor(object : ClickInterceptor<VH> {
        override fun onClick(position: Int, holder: VH) {
            block(position, holder)
        }
    })
    return this
}

/**
 * 监听item layout长按事件
 */
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.longClickInterceptor(crossinline block: (position: Int, holder: VH) -> Unit): Adapter {
    setInterceptor(object : LongClickInterceptor<VH> {
        override fun onLongClick(position: Int, holder: VH): Boolean {
            block(position, holder)
            return false
        }
    })
    return this
}

/**
 * View Holder创建时触发
 */
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.holderCreateInterceptor(crossinline block: (holder: VH) -> Unit): Adapter {
    setInterceptor(object : ViewHolderCreateInterceptor<VH> {
        override fun onCreateViewHolder(holder: VH) {
            block(holder)
        }
    })
    return this
}

/**
 * View Holder绑定时触发
 */
inline fun <T : Any, VH, Adapter : AbsRecyclerViewAdapter<T, VH>> Adapter.holderBindInterceptor(crossinline block: (position: Int, holder: VH) -> Unit): Adapter {
    setInterceptor(object : ViewHolderBindInterceptor<VH> {
        override fun onBindViewHolder(position: Int, holder: VH) {
            block(position, holder)
        }
    })
    return this
}