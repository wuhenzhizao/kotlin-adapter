package com.wuhenzhizao.adapter.holder

import android.util.SparseArray
import android.view.View
import com.wuhenzhizao.adapter.interfaces.ViewHolderSupport

/**
 * ViewHolder For AbsListView
 *
 * Created by liufei on 2017/12/4.
 */
class ListViewHolder(val convertView: View, val viewType: Int) : ViewHolderSupport {
    private var views: SparseArray<View> = SparseArray()
    var position: Int = 0

    init {
        convertView.tag = this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : View> getView(viewId: Int): T {
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

    inline fun <T : View> withView(viewId: Int, crossinline block: T.(view: T) -> Unit): ListViewHolder {
        val view = getView<T>(viewId)
        view.apply {
            block(view)
        }
        return this
    }
}