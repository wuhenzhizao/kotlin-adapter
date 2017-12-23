package com.wuhenzhizao.adapter.extension.stickyHeader

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.*
import com.wuhenzhizao.adapter.extension.R


/**
 * Created by liufei on 2017/12/16.
 */
class StickyHeaderTouchHelper(
        private val recyclerView: RecyclerView,
        private val decoration: StickyRecyclerItemDecoration)
    : RecyclerView.OnItemTouchListener {

    private val detector: GestureDetector

    init {
        detector = GestureDetector(recyclerView.context, SingleTapDetector())
    }

    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    override fun onInterceptTouchEvent(view: RecyclerView, e: MotionEvent): Boolean {
        val tapDetectorResponse = this.detector.onTouchEvent(e)
        if (tapDetectorResponse) {
            // Don't return false if a single tap is detected
            return true
        }
        if (e.action == MotionEvent.ACTION_DOWN) {
            val headerPos = decoration.findHeaderPositionUnder(e.x, e.y)
            val viewHolder = headerPos.first
            return viewHolder != null
        }
        return false
    }

    private inner class SingleTapDetector : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val headerPos = decoration.findHeaderPositionUnder(e.x, e.y)
            val viewHolder = headerPos.first
            val clickView = headerPos.second
            if (viewHolder != null) {
                performClick(clickView!!, e)
                val adapter = recyclerView.adapter as StickyRecyclerViewAdapter<*>
                val position = viewHolder.itemView.getTag(R.id.sticky_position) as Int
                adapter.innerHeaderClickListener?.apply {
                    onHeaderClick(viewHolder, clickView, position)
                }
                recyclerView.playSoundEffect(SoundEffectConstants.CLICK)
                clickView.onTouchEvent(e)
                return true
            }
            return false
        }

        private fun performClick(view: View, e: MotionEvent) {
            if (view is ViewGroup) {
                (0 until view.childCount)
                        .map { view.getChildAt(it) }
                        .forEach { performClick(it, e) }
            }

            containsBounds(view, e)
        }

        private fun containsBounds(view: View, e: MotionEvent): View? {
            val x = e.x.toInt()
            val y = e.y.toInt()
            val rect = Rect()
            view.getHitRect(rect)
            if (view.visibility == View.VISIBLE
                    && view.dispatchTouchEvent(e)
                    && rect.left < rect.right
                    && rect.top < rect.bottom
                    && x >= rect.left
                    && x < rect.right
                    && y >= rect.top) {
                view.performClick()
                return view
            }
            return null
        }

        override fun onDoubleTap(e: MotionEvent): Boolean {
            return true
        }
    }
}