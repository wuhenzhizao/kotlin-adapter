package com.wuhenzhizao.adapter.example.adapter

import android.databinding.BindingAdapter
import android.view.View
import com.gome.common.image.DraweeImageView
import com.gome.common.image.GImageLoader

/**
 * Created by liufei on 2017/12/15.
 */
object BindingAdapters {

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImage(iv: DraweeImageView, imageUrl: String) {
        GImageLoader.displayUrl(iv.context, iv, imageUrl)
    }

    @JvmStatic
    @BindingAdapter("android:selected")
    fun setSelected(v: View, selected: Boolean) {
        v.isSelected = selected
    }
}