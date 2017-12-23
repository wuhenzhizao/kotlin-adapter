package com.wuhenzhizao.adapter.extension.swipeMenu

import android.content.Context
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.implments.SwipeItemRecyclerMangerImpl
import com.daimajia.swipe.interfaces.SwipeAdapterInterface
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface
import com.daimajia.swipe.util.Attributes
import com.wuhenzhizao.adapter.RecyclerViewAdapter
import com.wuhenzhizao.adapter.extension.R
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder

/**
 * Created by liufei on 2017/12/4.
 */
class SwipeMenuRecyclerViewAdapter<T : Any>(context: Context, items: List<T>?) : RecyclerViewAdapter<T>(context, items), SwipeAdapterInterface, SwipeItemMangerInterface {
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

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        bindSwipeListener(holder, position)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int, payloads: MutableList<Any>?) {
        super.onBindViewHolder(holder, position, payloads)
        bindSwipeListener(holder, position)
    }

    private fun bindSwipeListener(holder: RecyclerViewHolder, position: Int) {
        if (!holder.has<SwipeLayout>(R.id.swipe_layout)) {
            return
        }
        holder.withView<SwipeLayout>(R.id.swipe_layout, {
            mItemManger.bindView(holder.itemView, position)
            addSwipeListener(object : SwipeLayout.SwipeListener {
                override fun onOpen(layout: SwipeLayout) {
                    closeAllExcept(layout)
                }

                override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {}

                override fun onStartOpen(layout: SwipeLayout?) {}

                override fun onStartClose(layout: SwipeLayout?) {}

                override fun onHandRelease(layout: SwipeLayout?, xVel: Float, yVel: Float) {}

                override fun onClose(layout: SwipeLayout?) {}
            })
        })
    }
}
