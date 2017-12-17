package com.wuhenzhizao.adapter.example.widget

import android.content.Context
import android.util.AttributeSet
import com.wuhenzhizao.adapter.example.image.DraweeImageView
import com.wuhenzhizao.adapter.example.R

/**
 * Created by liufei on 2017/10/17.
 */
class RatioImageView : DraweeImageView {
    var widthPercent: Int = 0
    var heightPercent: Int = 0

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initValues(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initValues(context, attrs)
    }

    private fun initValues(context: Context, attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView)

        widthPercent = array.getInteger(R.styleable.RatioImageView_widthPercent, 1)
        heightPercent = array.getInteger(R.styleable.RatioImageView_heightPercent, 1)

        array.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        if (widthMode == MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY) {
            // 1. 根据布局宽度推算图片宽度
            val imageWidth = widthSize - paddingLeft - paddingRight
            // 2. 根据图片宽度和宽高比,推算图片高度
            val imageHeight = imageWidth * heightPercent / widthPercent
            // 3. 根据图片高度, 推算布局高度
            val heightSize = imageHeight + paddingTop + paddingBottom
            // 4. 根据布局高度, 推算heightMeasureSpec
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }


}
