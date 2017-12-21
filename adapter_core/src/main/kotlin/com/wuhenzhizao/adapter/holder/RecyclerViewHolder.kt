package com.wuhenzhizao.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.wuhenzhizao.adapter.interfaces.ViewHolderDelegate


/**
 * Created by liufei on 2017/12/3.
 */
class RecyclerViewHolder(override val convertView: View) : RecyclerView.ViewHolder(convertView), ViewHolderDelegate {
    var layoutId: Int = 0

    fun <T : View> has(viewId: Int): Boolean = convertView.findViewById<T>(viewId) != null

    override fun <T : View> get(viewId: Int): T {
        val view = convertView.findViewById<T>(viewId)
        if (view != null) {
            return view as T
        }
        throw Exception("The specified view id is not found")
    }

    inline fun <T : View> get(viewId: Int, crossinline block: T.(view: T) -> Unit): T {
        val view = get<T>(viewId)
        view.apply {
            block(view)
        }
        return view
    }
}