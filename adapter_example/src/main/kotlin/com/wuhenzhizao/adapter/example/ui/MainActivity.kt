package com.wuhenzhizao.adapter.example.ui

import android.Manifest
import com.wuhenzhizao.adapter.example.R
import com.wuhenzhizao.adapter.example.adapter.MainTabFragmentAdapter
import com.wuhenzhizao.adapter.example.databinding.ActivityMainBinding
import com.wuhenzhizao.titlebar.utils.AppUtils
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getContentViewId(): Int = R.layout.activity_main

    override fun initViews() {
        binding.vpMain.adapter = MainTabFragmentAdapter(this, supportFragmentManager)
        binding.tabMain.setViewPager(binding.vpMain)

        val params = binding.viewStatusBar.layoutParams
        params.height = AppUtils.getStatusBarHeight(this)

        checkPermissions()
    }

    private fun checkPermissions() {
        AndPermission.with(this)
                .requestCode(0)
                .permission(
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(object : PermissionListener {
                    override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>) {
                    }

                    override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>) {
//                        if (AndPermission.hasAlwaysDeniedPermission(this@MainActivity, deniedPermissions)) {
//                            AndPermission.defaultSettingDialog(this@MainActivity).show()
//                        }
                    }
                })
                .rationale { _, rationale ->
                    AndPermission.rationaleDialog(this@MainActivity, rationale)
                }.start()
    }
}
