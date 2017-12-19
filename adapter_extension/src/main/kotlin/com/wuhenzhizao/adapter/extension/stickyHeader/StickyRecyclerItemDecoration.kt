package com.wuhenzhizao.adapter.extension.stickyHeader

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder

/**
 * Created by liufei on 2017/12/4.
 */
class StickyRecyclerItemDecoration(var adapter: StickyAdapterInterface<RecyclerViewHolder>, private var renderInline: Boolean) : RecyclerView.ItemDecoration() {

    companion object {
        val NO_HEADER_ID = -1L
    }

    private var mHeaderCache: MutableMap<Long, RecyclerViewHolder> = hashMapOf()

    /**
     * @param adapter the sticky header adapter to use
     */
    constructor(adapter: StickyAdapterInterface<RecyclerViewHolder>) : this(adapter, false)

    /**
     * {@inheritDoc}
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        val position = parent.getChildAdapterPosition(view)
        var headerHeight = 0

        if (position != RecyclerView.NO_POSITION
                && hasHeader(position)
                && showHeaderAboveItem(position)) {
            val header = getHeader(parent, position).itemView
            headerHeight = getHeaderHeightForLayout(header)
        }

        outRect.set(0, headerHeight, 0, 0)
    }

    private fun showHeaderAboveItem(itemAdapterPosition: Int): Boolean {
        return itemAdapterPosition == 0
                || adapter.getHeaderId(itemAdapterPosition - 1) != adapter.getHeaderId(itemAdapterPosition)
    }

    /**
     * Clears the header view cache. Headers will be recreated and
     * rebound on list scroll after this method has been called.
     */
    fun clearHeaderCache() {
        mHeaderCache.clear()
    }

    /**
     * Gets the position of the header under the specified (x, y) coordinates.
     *
     * @param x x-coordinate
     * @param y y-coordinate
     */
    fun findHeaderPositionUnder(x: Float, y: Float): Pair<RecyclerViewHolder?, View?> {
        for (holder in mHeaderCache.values) {
            val child = holder.itemView
            val translationX = child.translationX
            val translationY = child.translationY
            if (x >= child.left + translationX &&
                    x <= child.right + translationX &&
                    y >= child.top + translationY &&
                    y <= child.bottom + translationY) {
                return Pair(holder, child)
            }
        }
        return Pair(null, null)
    }


    private fun hasHeader(position: Int): Boolean {
        return adapter.getHeaderId(position) != NO_HEADER_ID
    }

    fun getHeader(parent: RecyclerView, position: Int): RecyclerViewHolder {
        val key = adapter.getHeaderId(position)

        if (mHeaderCache.containsKey(key)) {
            return mHeaderCache[key]!!
        } else {
            val holder = adapter.onCreateHeaderViewHolder(parent, position)
            val header = holder!!.itemView
            var params: ViewGroup.LayoutParams? = header.layoutParams
            if (params == null) {
                params = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            }

            adapter.onBindHeaderViewHolder((holder as RecyclerViewHolder?)!!, position)

            val widthSpec = View.MeasureSpec.makeMeasureSpec(parent.measuredWidth, View.MeasureSpec.EXACTLY)
            val heightSpec = View.MeasureSpec.makeMeasureSpec(parent.measuredHeight, View.MeasureSpec.UNSPECIFIED)

            val childWidth = ViewGroup.getChildMeasureSpec(widthSpec,
                    parent.paddingLeft + parent.paddingRight, params.width)
            val childHeight = ViewGroup.getChildMeasureSpec(heightSpec,
                    parent.paddingTop + parent.paddingBottom, params.height)

            header.measure(childWidth, childHeight)
            header.layout(0, 0, header.measuredWidth, header.measuredHeight)

            mHeaderCache.put(key, holder)

            return holder
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onDrawOver(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        val count = parent.childCount
        var previousHeaderId: Long = -1

        for (layoutPos in 0 until count) {
            val child = parent.getChildAt(layoutPos)
            val adapterPos = parent.getChildAdapterPosition(child)
            if (adapterPos != RecyclerView.NO_POSITION && hasHeader(adapterPos)) {
                val headerId = adapter.getHeaderId(adapterPos)

                if (headerId != previousHeaderId) {
                    previousHeaderId = headerId
                    val header = getHeader(parent, adapterPos).itemView
                    canvas.save()

                    val left = child.left
                    val top = getHeaderTop(parent, child, header, adapterPos, layoutPos)
                    canvas.translate(left.toFloat(), top.toFloat())

                    header.translationX = left.toFloat()
                    header.translationY = top.toFloat()
                    header.draw(canvas)
                    canvas.restore()
                }
            }
        }
    }

    private fun getHeaderTop(parent: RecyclerView, child: View, header: View, adapterPos: Int, layoutPos: Int): Int {
        val headerHeight = getHeaderHeightForLayout(header)
        var top = child.y.toInt() - headerHeight
        if (layoutPos == 0) {
            val count = parent.childCount
            val currentId = adapter.getHeaderId(adapterPos)
            // find next view with header and compute the offscreen push if needed
            for (i in 1 until count) {
                val adapterPosHere = parent.getChildAdapterPosition(parent.getChildAt(i))
                if (adapterPosHere != RecyclerView.NO_POSITION) {
                    val nextId = adapter.getHeaderId(adapterPosHere)
                    if (nextId != currentId && hasHeader(adapterPosHere)) {
                        val next = parent.getChildAt(i)
                        val offset = next.y.toInt() - (headerHeight + getHeader(parent, adapterPosHere).itemView.height)
                        return if (offset < 0) {
                            offset
                        } else {
                            break
                        }
                    }
                }
            }

            top = Math.max(0, top)
        }

        return top
    }

    private fun getHeaderHeightForLayout(header: View): Int {
        return if (renderInline) 0 else header.height
    }
}
