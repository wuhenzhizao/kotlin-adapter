package com.wuhenzhizao.adapter.interfaces

import android.support.v7.widget.RecyclerView

/**
 * Created by liufei on 2017/12/3.
 */
interface Interceptor<in T : Any, in VH : RecyclerView.ViewHolder>

/**
 * 布局拦截器
 */
interface LayoutInterceptor<in T : Any, in VH : RecyclerView.ViewHolder> : Interceptor<T, VH> {
    /**
     * 返回布局资源id
     *
     * @param position
     * @param item
     */
    fun getLayoutId(position: Int, item: T): Int?
}

/**
 * 单击事件拦截
 */
interface ClickInterceptor<in T : Any, in VH : RecyclerView.ViewHolder> : Interceptor<T, VH> {
    /**
     * @param position
     * @param item
     * @param vh
     */
    fun onClick(position: Int, item: T, vh: VH)
}

/**
 * 长按事件拦截
 */
interface LongClickInterceptor<in T : Any, in VH : RecyclerView.ViewHolder> : Interceptor<T, VH> {
    /**
     * @param position
     * @param item
     * @param vh
     */
    fun onLongClick(position: Int, item: T, vh: VH)
}

/**
 * RecyclerView.Adapter#onCreateViewHolder()运行时拦截
 */
interface ViewHolderCreateInterceptor<in T : Any, in VH : RecyclerView.ViewHolder> : Interceptor<T, VH> {
    /**
     * @param vh
     */
    fun onCreateViewHolder(vh: VH)
}

/**
 * RecyclerView.Adapter#onBindViewHolder()运行时拦截
 */
interface ViewHolderBindInterceptor<in T : Any, in VH : RecyclerView.ViewHolder> : Interceptor<T, VH> {
    /**
     * @param position
     * @param item
     * @param vh
     */
    fun onBindViewHolder(position: Int, item: T, vh: VH)
}