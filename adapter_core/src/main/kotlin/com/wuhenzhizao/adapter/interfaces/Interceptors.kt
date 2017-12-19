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
     * @param holder
     */
    fun onClick(position: Int, holder: VH)
}

/**
 * 长按事件拦截
 */
interface LongClickInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position
     * @param holder
     */
    fun onLongClick(position: Int, holder: VH)
}

/**
 * RecyclerView.Adapter#onCreateViewHolder()运行时拦截
 */
interface ViewHolderCreateInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param holder
     */
    fun onCreateViewHolder(holder: VH)
}

/**
 * RecyclerView.Adapter#onBindViewHolder()运行时拦截
 */
interface ViewHolderBindInterceptor<in VH> : Interceptor<VH> {
    /**
     * @param position
     * @param holder
     */
    fun onBindViewHolder(position: Int, holder: VH)
}