package com.wuhenzhizao.adapter

import android.content.Context
import android.view.LayoutInflater
import android.widget.AbsListView
import android.widget.BaseAdapter
import com.wuhenzhizao.adapter.extension.addItems
import com.wuhenzhizao.adapter.interfaces.Interceptor
import com.wuhenzhizao.adapter.interfaces.LayoutInterceptor
import com.wuhenzhizao.adapter.interfaces.ViewHolderBindInterceptor
import com.wuhenzhizao.adapter.interfaces.ViewHolderCreateInterceptor
import kotlin.reflect.KClass

/**
 * Created by liufei on 2017/12/4.
 */
abstract class AbsListViewAdapter<T : Any, VH>(context: Context) : BaseAdapter() {
    val items: MutableList<T> = arrayListOf()
    protected val itemTypes: MutableMap<KClass<*>, ItemTypeChain> = hashMapOf()
    protected val inflater: LayoutInflater = LayoutInflater.from(context)
    protected var layoutInterceptor: LayoutInterceptor<T, VH>? = null
    protected var viewHolderCreateInterceptor: ViewHolderCreateInterceptor<T, VH>? = null
    protected var viewHolderBindInterceptor: ViewHolderBindInterceptor<T, VH>? = null

    constructor(context: Context, items: List<T>?) : this(context) {
        if (items != null) {
            addItems(items)
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
        } else if (layoutInterceptor != null) {
            return layoutInterceptor!!.run {
                getLayoutId(position, item)
            }
        }
        throw RuntimeException("Could not found the specified class")
    }

    fun setInterceptor(interceptor: Interceptor<T, VH>) {
        when (interceptor) {
            is LayoutInterceptor<T, VH> -> layoutInterceptor = interceptor
            is ViewHolderCreateInterceptor<T, VH> -> viewHolderCreateInterceptor = interceptor
            is ViewHolderBindInterceptor<T, VH> -> viewHolderBindInterceptor = interceptor
        }
    }

    fun items(items: Collection<T>): AbsListViewAdapter<T, VH> {
        this.items.clear()
        this.items.addAll(items)
        return this
    }

    inline fun <reified KC> match(itemLayoutId: Int): AbsListViewAdapter<T, VH> {
        itemTypes.put(KC::class, ItemTypeChain(KC::class, itemLayoutId))
        return this
    }

    inline fun layoutInterceptor(crossinline block: (position: Int, item: T) -> Int): AbsListViewAdapter<T, VH> {
        setInterceptor(object : LayoutInterceptor<T, VH> {
            override fun getLayoutId(position: Int, item: T): Int = block.invoke(position, item)
        })
        return this
    }

    inline fun viewHolderCreateInterceptor(crossinline block: (vh: VH) -> Unit): AbsListViewAdapter<T, VH> {
        setInterceptor(object : ViewHolderCreateInterceptor<T, VH> {
            override fun onCreateViewHolder(vh: VH) {
                block.invoke(vh)
            }
        })
        return this
    }

    inline fun viewHolderBindInterceptor(crossinline block: (position: Int, item: T, vh: VH) -> Unit): AbsListViewAdapter<T, VH> {
        setInterceptor(object : ViewHolderBindInterceptor<T, VH> {
            override fun onBindViewHolder(position: Int, item: T, vh: VH) {
                block.invoke(position, item, vh)
            }
        })
        return this
    }

    fun attach(absListView: AbsListView): AbsListViewAdapter<T, VH> {
        absListView.adapter = this
        return this
    }
}