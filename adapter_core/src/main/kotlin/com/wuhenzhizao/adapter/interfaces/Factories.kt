package com.wuhenzhizao.adapter.interfaces

interface Factory

/**
 * 布局拦截器，根据position，返回需要的布局资源id
 */
interface LayoutFactory : Factory {
    /**
     * 获取布局资源id
     *
     * @param position adapter position
     * @return item layout resource id
     */
    fun getLayoutId(position: Int): Int
}
