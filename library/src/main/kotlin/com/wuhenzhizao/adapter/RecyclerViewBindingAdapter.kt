package com.wuhenzhizao.adapter

import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.OnRebindCallback
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.wuhenzhizao.adapter.extentions.getItem
import com.wuhenzhizao.adapter.holder.BindingViewHolder

/**
 * Created by liufei on 2017/12/4.
 */
class RecyclerViewBindingAdapter<T : Any>(context: Context, items: List<T>?) : AbsRecyclerViewAdapter<T, BindingViewHolder<ViewDataBinding>>(context, items) {
    private val DATA_INVALIDATION = Any()

    override fun getItemCount(): Int = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BindingViewHolder<ViewDataBinding> {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, viewType, null, false)
        val holder = BindingViewHolder(binding)
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
        viewHolderCreateInterceptor!!.apply {
            onCreateViewHolder(holder)
        }
        return holder
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, position: Int) {
        holder.binding.executePendingBindings()
        clickInterceptor!!.apply {
            holder.itemView.setOnClickListener {
                onClick(position, getItem(position), holder)
            }
        }
        longClickInterceptor!!.apply {
            holder.itemView.setOnClickListener {
                onLongClick(position, getItem(position), holder)
            }
        }
        viewHolderBindInterceptor!!.apply {
            onBindViewHolder(position, getItem(position), holder)
        }
    }

    override fun onBindViewHolder(holder: BindingViewHolder<ViewDataBinding>, position: Int, payloads: MutableList<Any>) {
        if (isForDataBinding(payloads)) {
            holder.binding.executePendingBindings()
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun isForDataBinding(payloads: List<Any>): Boolean {
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