package com.wuhenzhizao.adapter.example

import android.support.multidex.MultiDexApplication
import com.gome.common.image.GImageLoader
import okhttp3.OkHttpClient

/**
 * Created by liufei on 2017/12/14.
 */
class AppGlobal : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        GImageLoader.init(this, OkHttpClient.Builder().build())
    }
}