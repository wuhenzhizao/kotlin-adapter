package com.wuhenzhizao.adapter.example.image

import android.app.Application
import android.content.Context
import com.facebook.binaryresource.FileBinaryResource
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory
import com.facebook.imagepipeline.common.Priority
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.core.ImagePipelineFactory
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.request.ImageRequest
import okhttp3.OkHttpClient
import java.io.File

/**
 * Created by liufei on 2017/10/11.
 */
object GImageLoader {
    /**
     * Frasco初始化

     * @param context
     */
    fun init(context: Application, okHttpClient: OkHttpClient) {
        Fresco.initialize(context, ImageCore.getOkHttpImagePipelineConfig(context, okHttpClient))
    }

    /**
     * 通过图片资源ID，显示图片

     * @param callerContext
     * *
     * @param view
     * *
     * @param resId         ：app内部图片ID
     */
    fun displayRes(callerContext: Context, view: SimpleDraweeView, resId: Int) {
        ImageCore.load(callerContext, view, resId)
    }

    /**
     * 显示Url图片, 原图

     * @param callerContext
     * *
     * @param view
     * *
     * @param url           : 图片地址
     * *                      支持的类型：
     * *                      远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案
     * *                      本地文件	file://	FileInputStream
     * *                      Content provider	content://	ContentResolver
     * *                      asset目录下的资源	asset://	AssetManager
     * *                      res目录下的资源	res://	Resources.openRawResource
     */
    fun displayUrl(callerContext: Context, view: SimpleDraweeView, url: String) {
        display(callerContext, view, url, null, null, null, null, null, null, null)
    }

    /**
     * 加载本地图片，并进行压缩展示

     * @param callerContext
     * *
     * @param view
     * *
     * @param url
     * *
     * @param options
     */
    fun displayLocalUrl(callerContext: Context, view: SimpleDraweeView, url: String, options: ResizeOptions) {
        display(callerContext, view, url, null, null, options, null, null, null, null)
    }

    /**
     * 显示Url图片，使用切图服务
     * 应用场景：获取服务端指定大小图片

     * @param callerContext
     * *
     * @param view
     * *
     * @param url
     * *
     * @param width
     * *
     * @param ratio
     */
    fun displayResizeUrl(callerContext: Context, view: SimpleDraweeView, url: String, width: ImageWidth, ratio: AspectRatio) {
        display(callerContext, view, url, null, null, null, width, ratio, ImageRequest.RequestLevel.FULL_FETCH, null)
    }

    /**
     * 显示图片总方法

     * @param callerContext
     * *
     * @param view
     * *
     * @param url
     * *
     * @param thumbnailUrl
     * *
     * @param priority
     * *
     * @param options
     * *
     * @param width
     * *
     * @param ratio
     */
    fun display(
            callerContext: Context,
            view: SimpleDraweeView,
            url: String,
            thumbnailUrl: String?,
            priority: Priority?,
            options: ResizeOptions?,
            width: ImageWidth?,
            ratio: AspectRatio?,
            level: ImageRequest.RequestLevel?,
            listener: GImageLoadListener<ImageInfo>?) {
        ImageCore.load(callerContext, view, url, thumbnailUrl, priority, options, level, false, false, listener)
    }

    /**
     * 清理内存缓存，在内存不足时手动调用
     */
    fun clearMemoryCache() {
        Fresco.getImagePipeline().clearMemoryCaches()
    }

    /**
     * 清理硬盘缓存
     */
    fun clearDiskCache() {
        val factory = Fresco.getImagePipelineFactory()
        factory.imagePipeline.clearDiskCaches()
    }

    /**
     * 获取缓存大小

     * @return
     */
    fun getDiskCacheSize(): Double = Fresco.getImagePipelineFactory().mainFileCache.size.toDouble()

    /**
     * 获取fresco本地缓存图片

     * @param url
     */
    fun getDiskCache(url: String?): File? {
        var targetFile: File? = null
        if (url != null) {
            val cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(ImageRequest.fromUri(url))
            if (ImagePipelineFactory.getInstance().mainFileCache.hasKey(cacheKey)) {
                val resource = ImagePipelineFactory.getInstance().mainFileCache.getResource(cacheKey)
                targetFile = (resource as FileBinaryResource).file
            } else if (ImagePipelineFactory.getInstance().smallImageFileCache.hasKey(cacheKey)) {
                val resource = ImagePipelineFactory.getInstance().smallImageFileCache.getResource(cacheKey)
                targetFile = (resource as FileBinaryResource).file
            }
        }
        return targetFile
    }

    /**
     * 释放图片缓存资源
     */
    fun releaseResources() {
        Fresco.shutDown()
    }

    /**
     * 是否在移动网络下显示图片

     * @param showImageInMobileNetwork
     */
    fun setShowImageInMobileNetwork(showImageInMobileNetwork: Boolean) {
        ImageCore.setUndisplayOutOffWifiNetWork(showImageInMobileNetwork)
    }
}