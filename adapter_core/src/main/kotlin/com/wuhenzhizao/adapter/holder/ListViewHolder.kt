package com.wuhenzhizao.adapter.holder

import android.util.SparseArray
import android.view.View

/**
 * Created by liufei on 2017/12/4.
 */
class ListViewHolder(val convertView: View) {
    private var views: SparseArray<View> = SparseArray()

    init {
        convertView.tag = this
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T : View> get(id: Int): T {
        var childView: View? = views.get(id)
        if (childView == null) {
            childView = convertView.findViewById(id)
            views.put(id, childView)
        }
        return childView as T
    }
}