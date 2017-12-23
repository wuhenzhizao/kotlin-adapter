package com.wuhenzhizao.adapter.interfaces

import android.view.View


interface ViewHolderSupport {

    fun <T : View> getView(viewId: Int): T
}
