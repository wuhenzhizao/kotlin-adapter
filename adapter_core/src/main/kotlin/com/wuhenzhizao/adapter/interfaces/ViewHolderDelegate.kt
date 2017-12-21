package com.wuhenzhizao.adapter.interfaces

import android.view.View


/**
 * Created by liufei on 2017/12/21.
 */
interface ViewHolderDelegate {
    val convertView: View

    fun <T : View> get(viewId: Int): T
}