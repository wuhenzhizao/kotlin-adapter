package com.wuhenzhizao.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.wuhenzhizao.adapter.interfaces.ViewHolderSupport


/**
 * Created by liufei on 2017/12/3.
 */
class RecyclerViewHolder(private val convertView: View, val viewType: Int) : RecyclerView.ViewHolder(convertView), ViewHolderSupport {


    fun <T : View> has(viewId: Int): Boolean = convertView.findViewById<T>(viewId) != null

    override fun <T : View> getView(viewId: Int): T {
        val view = convertView.findViewById<T>(viewId)
        if (view != null) {
            return view as T
        }
        throw Exception("The specified view id is not found")
    }

    inline fun <T : View> withView(viewId: Int, crossinline block: T.(view: T) -> Unit): RecyclerViewHolder {
        val view = getView<T>(viewId)
        view.apply {
            block(view)
        }
        return this
    }
}