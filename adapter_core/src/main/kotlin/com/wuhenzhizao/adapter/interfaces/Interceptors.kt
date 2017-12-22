package com.wuhenzhizao.adapter.interfaces

/**
 * Created by liufei on 2017/12/3.
 */
interface Interceptor<in VH>

/**
 * 布局拦截器，根据position，返回需要的布局资源id
 */
interface LayoutInterceptor<in VH> : Interceptor<VH> {
    /**
     * 获取布局资源id
     *
     * @param position adapter position
     * @return item layout resource id
     */
    fun getLayoutId(position: Int): Int
}

/**
 * 单击事件拦截，item layout被点击时触发
 */
interface ClickInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position adapter position
     * @param holder
     */
    fun onClick(position: Int, holder: VH)
}

/**
 * 长按事件拦截, item layout长按时触发
 */
interface LongClickInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position adapter position
     * @param holder
     * @return true if the callback consumed the long click, false otherwise.
     */
    fun onLongClick(position: Int, holder: VH): Boolean
}

/**
 * View Holder创建时触发，用于完成视图的初始化
 *
 */
interface ViewHolderCreateInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param holder adapter position
     */
    fun onCreateViewHolder(holder: VH)
}

/**
 * View Holder更新时触发，用于更新Item数据
 */
interface ViewHolderBindInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position adapter position
     * @param holder
     */
    fun onBindViewHolder(position: Int, holder: VH)
}