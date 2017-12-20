package com.wuhenzhizao.adapter.example.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ListView

/**
 * Created by liufei on 2017/12/20.
 */
class ExtendListView : ListView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val mExpandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE shr 2, View.MeasureSpec.AT_MOST)
        super.onMeasure(widthMeasureSpec, mExpandSpec)
    }
}