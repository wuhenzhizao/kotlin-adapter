package com.wuhenzhizao.adapter.extension.swipeMenu

import android.content.Context
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface
import com.daimajia.swipe.util.Attributes
import com.wuhenzhizao.adapter.extension.R
import com.wuhenzhizao.adapter.extension.stickyHeader.StickyBean
import com.wuhenzhizao.adapter.extension.stickyHeader.StickyRecyclerViewAdapter

/**
 * Created by liufei on 2017/12/4.
 */
class SwipeMenuStickyRecyclerViewAdapter<T : StickyBean>(context: Context, items: List<T>?) : StickyRecyclerViewAdapter<T>(context, items), SwipeAdapterInterface, SwipeItemMangerInterface {
    private val mItemManger = SwipeItemRecyclerMangerImpl(this)

    constructor(context: Context) : this(context, null)

    override fun getSwipeLayoutResourceId(position: Int): Int {
        return R.id.swipe_layout
    }

    override fun openItem(position: Int) {
        mItemManger.openItem(position)
    }

    override fun closeItem(position: Int) {
        mItemManger.closeItem(position)
    }

    override fun closeAllExcept(layout: SwipeLayout) {
        mItemManger.closeAllExcept(layout)
    }

    override fun closeAllItems() {
        mItemManger.closeAllItems()
    }

    override fun getOpenItems(): List<Int> {
        return mItemManger.openItems
    }

    override fun getOpenLayouts(): List<SwipeLayout> {
        return mItemManger.openLayouts
    }

    override fun removeShownLayouts(layout: SwipeLayout) {
        mItemManger.removeShownLayouts(layout)
    }

    override fun isOpen(position: Int): Boolean {
        return mItemManger.isOpen(position)
    }

    override fun getMode(): Attributes.Mode {
        return mItemManger.mode
    }

    override fun setMode(mode: Attributes.Mode) {
        mItemManger.mode = mode
    }
}
