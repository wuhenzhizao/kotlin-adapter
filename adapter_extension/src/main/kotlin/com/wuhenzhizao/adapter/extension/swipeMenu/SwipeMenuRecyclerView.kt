package com.wuhenzhizao.adapter.extension.swipeMenu

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.daimajia.swipe.SwipeLayout
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface
import java.util.*

/**
 * Created by liufei on 2017/12/20.
 */
class SwipeMenuRecyclerView : RecyclerView {
    private val INVALID_POSITION = -1
    private var oldSwipeLayout: SwipeLayout? = null
    private var isClosing = false
    private var mOldTouchedPosition = INVALID_POSITION

    private var lastDispatchX = 0
    private var lastDispatchY = 0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    fun getSwipeItemManager(): SwipeItemMangerInterface {
        return adapter as SwipeItemMangerInterface
    }

    private val isOldSwipeLayoutStatusOpen: Boolean
        get() = oldSwipeLayout != null && oldSwipeLayout?.openStatus != SwipeLayout.Status.Close

    private fun init() {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    getSwipeItemManager().closeAllItems()
                }
            }
        })
    }

    private fun getSwipeLayoutView(itemView: View): SwipeLayout? {
        if (itemView is SwipeLayout) {
            return itemView
        }
        val unvisited = ArrayList<View>()
        unvisited.add(itemView)
        while (!unvisited.isEmpty()) {
            val child = unvisited.removeAt(0) as? ViewGroup ?: continue
            if (child is SwipeLayout) {
                return child
            }
            val childCount = child.childCount
            (0 until childCount).mapTo(unvisited) { child.getChildAt(it) }
        }
        return null
    }

    private fun closeSwipeLayout() {
        isClosing = true
        oldSwipeLayout!!.close(true, false)
        oldSwipeLayout!!.postDelayed({ isClosing = false }, 500)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val currentDispatchX = ev.x.toInt()
        val currentDispatchY = ev.y.toInt()
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> parent.requestDisallowInterceptTouchEvent(true)
            MotionEvent.ACTION_MOVE ->
                if (Math.abs(currentDispatchX - lastDispatchX) < Math.abs(currentDispatchY - lastDispatchY)) {
                    parent.requestDisallowInterceptTouchEvent(false)
                }
        }
        lastDispatchX = currentDispatchX
        lastDispatchY = currentDispatchY
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        if (e.pointerCount > 1) {
            return true
        }
        var isIntercepted = super.onInterceptTouchEvent(e)

        val touchingPosition: Int
        if (e.action == MotionEvent.ACTION_DOWN) {
            touchingPosition = getChildAdapterPosition(findChildViewUnder(e.x, e.y))
            if (touchingPosition != mOldTouchedPosition && isOldSwipeLayoutStatusOpen) {
                closeSwipeLayout()
                isIntercepted = true
            }
            if (isIntercepted) {
                oldSwipeLayout = null
                mOldTouchedPosition = INVALID_POSITION
            } else {
                val vh = findViewHolderForAdapterPosition(touchingPosition)
                if (vh != null) {
                    val swipeLayout = getSwipeLayoutView(vh.itemView)
                    if (swipeLayout != null) {
                        oldSwipeLayout = swipeLayout
                        mOldTouchedPosition = touchingPosition
                    }
                }
            }
        }
        return isIntercepted
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_MOVE -> if (isClosing) {
                return true
            }
        }
        return super.onTouchEvent(e)
    }
}