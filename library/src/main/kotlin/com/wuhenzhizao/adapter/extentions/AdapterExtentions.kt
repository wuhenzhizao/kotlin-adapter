package com.wuhenzhizao.adapter.extentions

import com.wuhenzhizao.adapter.AbsRecyclerViewAdapter

/**
 * 获取指定位置的数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.getItem(index: Int): T = items[index]

/**
 * 添加一项数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.addItem(item: T) {
    this.items.add(item)
    notifyDataChanged()
}

/**
 * 在指定位置添加数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.addItem(index: Int, item: T) {
    checkDataValid(index)
    this.items.add(index, item)
    notifyDataChanged()
}

/**
 * 添加数据集合
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.addItems(items: Collection<T>) {
    this.items.addAll(items)
    notifyDataChanged()
}

/**
 * 在指定位置添加数据集合
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.addItems(index: Int, items: Collection<T>) {
    checkDataValid(index)
    this.items.addAll(index, items)
    notifyDataChanged()
}

/**
 * 移除指定数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.removeItem(item: T) {
    this.items.remove(item)
    notifyDataChanged()
}

/**
 * 移除指定位置的数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.removeItemAt(index: Int) {
    checkDataValid(index)
    this.items.removeAt(index)
    notifyDataChanged()
}

/**
 * 移除指定位置及之后的所有数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.removeItemFrom(start: Int) {
    checkDataValid(start)
    val removedItems = this.items.filterIndexed { index, _ -> index >= start }
    this.items.removeAll(removedItems)
    notifyDataChanged()
}

/**
 * 移除指定范围的数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.removeItemRange(start: Int, end: Int) {
    checkDataValid(start)
    checkDataValid(end)
    val removedItems = this.items.filterIndexed { index, _ -> index in start..end }
    this.items.removeAll(removedItems)
    notifyDataChanged()
}

/**
 * 移除指定范围的数据
 */
fun <T : Any> AbsRecyclerViewAdapter<T, *>.clear() {
    this.items.clear()
    notifyDataChanged()
}

private fun <T : Any> AbsRecyclerViewAdapter<T, *>.checkDataValid(index: Int) {
    if (index < 0 || index >= this.items.size) {
        throw IndexOutOfBoundsException("Index must be large than zero and less than items' size")
    }
}
