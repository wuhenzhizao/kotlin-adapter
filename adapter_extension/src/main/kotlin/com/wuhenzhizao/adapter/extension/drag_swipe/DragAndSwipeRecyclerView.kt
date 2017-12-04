package com.wuhenzhizao.adapter.extension.drag_swipe

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet

/**
 * Created by liufei on 2017/12/4.
 */
class DragAndSwipeRecyclerView(context: Context, attrs: AttributeSet?, defStyle: Int) : RecyclerView(context) {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun getAdapter(): DragAndSwipeRecyclerViewAdapter<*> {
        return super.getAdapter() as DragAndSwipeRecyclerViewAdapter<*>
    }

    fun setAdapter(adapter: DragAndSwipeRecyclerViewAdapter<*>?) {
        val callback = ItemTouchHelperCallBack()
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(this)

        super.setAdapter(adapter)
    }

    private inner class ItemTouchHelperCallBack : ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean = true

        override fun isItemViewSwipeEnabled(): Boolean {
            return false
        }

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            val dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlag = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            return ItemTouchHelper.Callback.makeMovementFlags(dragFlag, swipeFlag)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            if (viewHolder.itemViewType != target.itemViewType) {
                return false
            }
            adapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.onItemDismiss(viewHolder.adapterPosition)
        }
    }
}