package com.wuhenzhizao.adapter.extension

import com.wuhenzhizao.adapter.AbsListViewAdapter
import com.wuhenzhizao.adapter.AbsRecyclerViewAdapter

/***************************************************************************
 *                                                                         *
 *                   AbsRecyclerViewAdapter属性拓展                         *
 *                                                                         *
 ***************************************************************************/

/**
 * 获取所有数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.getItems(): MutableList<T> = items

/**
 * 获取指定位置的数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.getItem(index: Int): T = items[index]

/**
 * 添加一项数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.addItem(item: T) {
    this.items.add(item)
    this.notifyItemInserted(this.items.size - 1)
}

/**
 * 在指定位置添加数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.addItem(index: Int, item: T) {
    checkDataValid(index)
    this.items.add(index, item)
    this.notifyItemInserted(index)
}

/**
 * 添加数据集合
 */
fun <T : Any, Adapter : AbsRecyclerViewAdapter<T, *>> Adapter.addItems(items: Collection<T>): Adapter {
    val previousIndex = this.items.size - 1
    this.items.addAll(items)
    this.notifyItemRangeInserted(previousIndex + 1, items.size)
    return this
}

/**
 * 在指定位置添加数据集合
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.addItems(index: Int, items: Collection<T>) {
    checkDataValid(index)
    this.items.addAll(index, items)
    this.notifyItemRangeInserted(index, items.size)
}

/**
 * 添加数据集合
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.putItems(items: Collection<T>) {
    val previousIndex = this.items.size - 1
    this.items.clear()
    this.items.addAll(items)
    this.notifyItemRangeInserted(previousIndex + 1, items.size)
}

/**
 * 移除指定位置的数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.removeItemAt(index: Int) {
    checkDataValid(index)
    this.items.removeAt(index)
    this.notifyItemRemoved(index)
}

/**
 * 移除指定位置及之后的所有数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.removeItemFrom(start: Int) {
    checkDataValid(start)
    val removedItems = this.items.filterIndexed { index, _ -> index >= start }
    this.items.removeAll(removedItems)
    this.notifyItemRangeRemoved(start, removedItems.size)
}

/**
 * 移除指定范围的数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.removeItemRange(start: Int, end: Int) {
    checkDataValid(start)
    checkDataValid(end)
    val removedItems = this.items.filterIndexed { index, _ -> index in start..end }
    this.items.removeAll(removedItems)
    this.notifyItemRangeRemoved(start, removedItems.size)
}

/**
 * 移除指定范围的数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.clear() {
    this.items.clear()
    this.notifyDataSetChanged()
}

private fun <T : Any> AbsRecyclerViewAdapter<T, *>.checkDataValid(index: Int) {
    if (index < 0 || index >= this.items.size) {
        throw IndexOutOfBoundsException("Index must be large than zero and less than items' size")
    }
}


/***************************************************************************
 *                                                                         *
 *                     AbsListViewAdapter属性拓展                           *
 *                                                                         *
 ***************************************************************************/

/**
 * 获取所有数据
 */
fun <T : Any> AbsListViewAdapter<T, *>.getItems(): MutableList<T> = items

/**
 * 获取指定位置的数据
 */
fun <T : Any> AbsListViewAdapter<T, *>.getItem(index: Int): T = items[index]

/**
 * 添加一项数据
 */
fun <T : Any> AbsListViewAdapter<T, *>.addItem(item: T) {
    this.items.add(item)
    this.notifyDataSetChanged()
}

/**
 * 在指定位置添加数据
 */
fun <T : Any> AbsListViewAdapter<T, *>.addItem(index: Int, item: T) {
    checkDataValid(index)
    this.items.add(index, item)
    this.notifyDataSetChanged()
}

/**
 * 添加数据集合
 */
fun <T : Any> AbsListViewAdapter<T, *>.addItems(items: Collection<T>) {
    this.items.addAll(items)
    this.notifyDataSetChanged()
}

/**
 * 添加数据集合
 */
fun <T : Any> AbsListViewAdapter<T, *>.putItems(items: Collection<T>) {
    this.items.clear()
    this.items.addAll(items)
    this.notifyDataSetChanged()
}

/**
 * 移除指定位置的数据
 */
fun <T : Any> AbsListViewAdapter<T, *>.removeItemAt(index: Int) {
    checkDataValid(index)
    this.items.removeAt(index)
    this.notifyDataSetChanged()
}

/**
 * 移除指定范围的数据
 */
fun <T : Any> AbsListViewAdapter<T, *>.removeItemRange(start: Int, end: Int) {
    checkDataValid(start)
    checkDataValid(end)
    val removedItems = this.items.filterIndexed { index, _ -> index in start..end }
    this.items.removeAll(removedItems)
    this.notifyDataSetChanged()
}

/**
 * 移除指定范围的数据
 */
fun <T : Any> AbsListViewAdapter<T, *>.clear() {
    this.items.clear()
    this.notifyDataSetChanged()
}

private fun <T : Any> AbsListViewAdapter<T, *>.checkDataValid(index: Int) {
    if (index < 0 || index >= this.items.size) {
        throw IndexOutOfBoundsException("Index must be large than zero and less than items' size")
    }
}




