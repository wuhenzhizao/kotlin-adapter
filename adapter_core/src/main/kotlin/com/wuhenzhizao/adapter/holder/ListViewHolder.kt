package com.wuhenzhizao.adapter.holder

import android.util.SparseArray
import android.view.View
import com.wuhenzhizao.adapter.interfaces.ViewHolderDelegate

/**
 * Created by liufei on 2017/12/4.
 */
class ListViewHolder(override val convertView: View) : ViewHolderDelegate {
    private var views: SparseArray<View> = SparseArray()
    var layoutId: Int = 0
    var position: Int = 0

    init {
        convertView.tag = this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : View> get(viewId: Int): T {
        var view = views.get(viewId)
        if (view == null) {
            view = convertView.findViewById(viewId)
            views.put(viewId, view)
        }
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