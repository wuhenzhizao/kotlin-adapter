package com.wuhenzhizao.adapter.holder

import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by liufei on 2017/12/3.
 */
class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    operator fun <T : View> get(id: Int): T {
        return itemView.findViewById(id) as T
    }
}