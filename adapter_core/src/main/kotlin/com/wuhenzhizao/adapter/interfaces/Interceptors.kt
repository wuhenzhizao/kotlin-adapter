package com.wuhenzhizao.adapter.interfaces

/**
 * Created by liufei on 2017/12/3.
 */
interface Interceptor<in VH>

/**
 * 布局拦截器
 */
interface LayoutInterceptor<in VH> : Interceptor<VH> {
    /**
     * 返回布局资源id
     *
     * @param position
     */
    fun getLayoutId(position: Int): Int
}

/**
 * 单击事件拦截
 */
interface ClickInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position
     * @param vh
     */
    fun onClick(position: Int, vh: VH)
}

/**
 * 长按事件拦截
 */
interface LongClickInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position
     * @param vh
     */
    fun onLongClick(position: Int, vh: VH)
}

/**
 * RecyclerView.Adapter#onCreateViewHolder()运行时拦截
 */
interface ViewHolderCreateInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param vh
     */
    fun onCreateViewHolder(vh: VH)
}

/**
 * RecyclerView.Adapter#onBindViewHolder()运行时拦截
 */
interface ViewHolderBindInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position
     * @param vh
     */
    fun onBindViewHolder(position: Int, vh: VH)
}