package com.wuhenzhizao.adapter.example.ui

import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.adapter.MainTabFragmentAdapter
import com.wuhenzhizao.adapter.example.databinding.ActivityMainBinding
import com.wuhenzhizao.titlebar.utils.AppUtils

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getContentViewId(): Int = R.layout.activity_main

    override fun initViews() {
        binding.vpMain.adapter = MainTabFragmentAdapter(this, supportFragmentManager)
        binding.tabMain.setViewPager(binding.vpMain)

        val params = binding.viewStatusBar.layoutParams
        params.height = AppUtils.getStatusBarHeight(this)
    }
}
