package com.wuhenzhizao.adapter.extension.dragSwipeDismiss

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.AttributeSet
import com.wuhenzhizao.adapter.holder.RecyclerViewHolder

/**
 * Created by liufei on 2017/12/4.
 */
class DragAndSwipeRecyclerView(context: Context, attrs: AttributeSet?, defStyle: Int) : RecyclerView(context, attrs, defStyle) {
    var isLongPressDragEnable: Boolean = false
    var isItemViewSwipeEnable: Boolean = false
    var dragDirection: Int = ItemTouchHelper.UP or ItemTouchHelper.DOWN
    var swipeDirection: Int = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    override fun getAdapter(): DragAndSwipeRecyclerViewAdapter<*> {
        return super.getAdapter() as DragAndSwipeRecyclerViewAdapter<*>
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        val callback = ItemTouchHelperCallBack()
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(this)

        super.setAdapter(adapter)
    }

    inner class ItemTouchHelperCallBack : ItemTouchHelper.Callback() {

        override fun isLongPressDragEnabled(): Boolean = isLongPressDragEnable

        override fun isItemViewSwipeEnabled(): Boolean = isItemViewSwipeEnable

        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            return ItemTouchHelper.Callback.makeMovementFlags(dragDirection, swipeDirection)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            if (viewHolder.itemViewType != target.itemViewType) {
                return false
            }
            adapter.onItemDrag(viewHolder as RecyclerViewHolder, target as RecyclerViewHolder)
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            adapter.onItemSwipe(viewHolder as RecyclerViewHolder, direction)
        }
    }
}