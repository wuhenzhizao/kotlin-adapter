package com.wuhenzhizao.adapter.extension.stickyHeader

/**
 * Created by liufei on 2017/12/4.
 */
open class StickyBean(var stickyId: Long = STICKY_NONE) {
    companion object {
        val STICKY_NONE: Long = -1
    }

    override fun toString(): String {
        return "StickyBean(stickyId=$stickyId)"
    }


}