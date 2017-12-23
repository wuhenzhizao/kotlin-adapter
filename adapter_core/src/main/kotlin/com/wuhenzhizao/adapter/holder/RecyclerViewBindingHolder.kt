package com.wuhenzhizao.adapter.holder

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View
import com.wuhenzhizao.adapter.interfaces.ViewHolderSupport

/**
 * Created by liufei on 2017/12/4.
 */
class RecyclerViewBindingHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root), ViewHolderSupport {

    override fun <T : View> getView(viewId: Int): T {
        return binding.root.findViewById(viewId)
    }

    inline fun <reified VB> getBinding(): VB {
        return binding as VB
    }
}