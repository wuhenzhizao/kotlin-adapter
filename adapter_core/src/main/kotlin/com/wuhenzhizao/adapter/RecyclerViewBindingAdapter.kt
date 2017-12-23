package com.wuhenzhizao.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.OnRebindCallback
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.wuhenzhizao.adapter.holder.RecyclerViewBindingHolder
import kotlin.reflect.KClass

/**
 * Created by liufei on 2017/12/4.
 */
open class RecyclerViewBindingAdapter<T : Any>(context: Context, items: List<T>?) : AbsRecyclerViewAdapter<T, RecyclerViewBindingHolder<ViewDataBinding>>(context, items) {
    private val DATA_INVALIDATION = Any()

    constructor(context: Context) : this(context, null)

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewBindingHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, parent, false)
        val holder = RecyclerViewBindingHolder(binding)
        binding.addOnRebindCallback(object : OnRebindCallback<ViewDataBinding>() {
            override fun onPreBind(binding: ViewDataBinding): Boolean = let {
                recyclerView!!.isComputingLayout
            }

            override fun onCanceled(binding: ViewDataBinding) {
                if (recyclerView!!.isComputingLayout) {
                    return
                }
                val position = holder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    notifyItemChanged(position, DATA_INVALIDATION)
                }
            }
        })
        innerHolderCreateListener?.apply {
            onCreateViewHolder(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: RecyclerViewBindingHolder<ViewDataBinding>, position: Int) {
        innerClickListener?.apply {
            holder.itemView.setOnClickListener {
                onClick(holder, position)
            }
        }
        innerLongClickListener?.apply {
            holder.itemView.setOnLongClickListener {
                onLongClick(holder, position)
            }
        }
        innerHolderBindListener?.apply {
            onBindViewHolder(holder, position)
        }
        updateViewHolder(holder, position)
    }

    override fun onBindViewHolder(holder: RecyclerViewBindingHolder<ViewDataBinding>, position: Int, payloads: MutableList<Any>) {
        if (isValidPayLoads(payloads)) {
            onBindViewHolder(holder, position)
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun updateViewHolder(holder: RecyclerViewBindingHolder<ViewDataBinding>, position: Int) {
        val item = getItem(position)
        val itemType = itemTypes[item::class]
        if (itemType != null) {
            holder.binding.setVariable(itemType.variableId, item)
            holder.binding.executePendingBindings()
        }
    }

    private fun isValidPayLoads(payloads: List<Any>): Boolean {
        if (payloads.isEmpty()) {
            return false
        }
        payloads.forEach {
            if (it != DATA_INVALIDATION) {
                return false
            }
        }
        return true
    }
}

/**
 * 建立数据类与布局文件之间的匹配关系
 * @param variableId BR.xx
 */
fun <T : Any, Adapter : RecyclerViewBindingAdapter<T>> Adapter.match(kClass: KClass<*>, itemLayoutId: Int, variableId: Int): Adapter {
    itemTypes.put(kClass, ItemType(itemLayoutId, variableId))
    return this
}