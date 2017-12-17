package com.wuhenzhizao.adapter.example.image

import android.content.Context
import android.databinding.BindingAdapter
import android.util.AttributeSet
import com.facebook.drawee.generic.GenericDraweeHierarchy
import com.facebook.drawee.view.SimpleDraweeView


/**
 * Created by liufei on 2017/10/13.
 */
open class DraweeImageView : SimpleDraweeView {

    constructor(context: Context, hierarchy: GenericDraweeHierarchy) : super(context, hierarchy)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

}