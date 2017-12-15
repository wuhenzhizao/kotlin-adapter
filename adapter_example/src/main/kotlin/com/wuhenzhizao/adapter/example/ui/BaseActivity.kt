package com.wuhenzhizao.adapter.example.ui

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wuhenzhizao.titlebar.utils.AppUtils

/**
 * Created by liufei on 2017/10/11.
 */
abstract class BaseActivity<DB : ViewDataBinding> : AppCompatActivity() {
    protected lateinit var binding: DB

    protected abstract fun getContentViewId(): Int

    protected abstract fun initViews()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getContentViewId())
        initViews()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        AppUtils.transparencyBar(window)
        AppUtils.StatusBarLightMode(window)
    }
}