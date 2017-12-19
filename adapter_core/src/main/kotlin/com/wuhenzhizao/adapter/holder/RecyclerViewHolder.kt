package com.wuhenzhizao.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View


/**
 * Created by liufei on 2017/12/3.
 */
class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    var layoutId: Int = 0

    fun <T : View> has(viewId: Int): Boolean = itemView.findViewById<T>(viewId) != null

    fun <T : View> get(viewId: Int): T {
        val view = itemView.findViewById<T>(viewId)
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