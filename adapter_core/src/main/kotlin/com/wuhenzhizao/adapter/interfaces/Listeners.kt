package com.wuhenzhizao.adapter.interfaces


interface Listener<in VH>

/**
 * 单击事件拦截，item layout被点击时触发
 */
interface ClickListener<in VH> : Listener<VH> {
    /**
     * @param holder
     * @param position adapter position
     */
    fun onClick(holder: VH, position: Int)
}

/**
 * 长按事件拦截, item layout长按时触发
 */
interface LongClickListener<in VH> : Listener<VH> {
    /**
     * @param holder
     * @param position adapter position
     * @return true if the callback consumed the long click, false otherwise.
     */
    fun onLongClick(holder: VH, position: Int): Boolean
}

/**
 * View Holder创建时触发，用于完成视图的初始化
 *
 */
interface ViewHolderCreateListener<in VH> : Listener<VH> {
    /**
     * @param holder adapter position
     */
    fun onCreateViewHolder(holder: VH)
}

/**
 * View Holder更新时触发，用于更新Item数据
 */
interface ViewHolderBindListener<in VH> : Listener<VH> {
    /**
     * @param holder
     * @param position adapter position
     * @param payloads A non-null list of merged payloads. Can be empty list if requires full update
     */
    fun onBindViewHolder(holder: VH, position: Int, payloads: MutableList<Any>?)
}