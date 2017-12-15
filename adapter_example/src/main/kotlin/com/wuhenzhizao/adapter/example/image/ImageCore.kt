package com.gome.common.image

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Animatable
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import com.facebook.cache.disk.DiskCacheConfig
import com.facebook.common.internal.Sets
import com.facebook.common.util.ByteConstants
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder
import com.facebook.drawee.controller.ControllerListener
import com.facebook.drawee.view.SimpleDraweeView
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory
import com.facebook.imagepipeline.cache.MemoryCacheParams
import com.facebook.imagepipeline.common.Priority
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
import com.facebook.imagepipeline.image.ImageInfo
import com.facebook.imagepipeline.listener.RequestListener
import com.facebook.imagepipeline.listener.RequestLoggingListener
import com.facebook.imagepipeline.request.ImageRequest
import com.facebook.imagepipeline.request.ImageRequestBuilder
import okhttp3.OkHttpClient

/**
 * Created by liufei on 2017/10/11.
 */
object ImageCore {
    /***
     * 最大可运行内存
     */
    private val MAX_HEAP_SIZE = Runtime.getRuntime().maxMemory().toInt()

    /**
     * 最大文件缓存限制
     */
    private val MAX_DISK_CACHE_SIZE = 40 * ByteConstants.MB

    /**
     * 最大内存限制
     */
    private val MAX_MEMORY_CACHE_SIZE = MAX_HEAP_SIZE / 4

    /**
     * 缓存目录
     */
    private val IMAGE_PIPELINE_CACHE_DIR = "Android/data/com.mx.os/FrescoCache"

    /**
     * 小缓存目录
     */
    private val IMAGE_PIPELINE_SMALL_CACHE_DIR = "Android/data/com.mx.os/FrescoSmallCache"

    /**
     * Pipeline配置
     */
    @SuppressLint("StaticFieldLeak")
    private var sOkHttpImagePipelineConfig: ImagePipelineConfig? = null

    /**
     * 非wifi环境是否显示图片
     */
    private var undisplayOutOffWifiNetWork = false

    /**
     * Creates config using OkHttp as network backed.
     */
    fun getOkHttpImagePipelineConfig(context: Context, okHttpClient: OkHttpClient): ImagePipelineConfig? {
        if (sOkHttpImagePipelineConfig == null) {
            val configBuilder = OkHttpImagePipelineConfigFactory.newBuilder(context, okHttpClient)
            configureCaches(context, configBuilder)
            configureLoggingListeners(configBuilder)
            sOkHttpImagePipelineConfig = configBuilder.build()
        }
        return sOkHttpImagePipelineConfig
    }

    /**
     * Configures disk and memory cache not to exceed common limits
     */
    private fun configureCaches(context: Context, configBuilder: ImagePipelineConfig.Builder) {
        val bitmapCacheParams = MemoryCacheParams(
                MAX_MEMORY_CACHE_SIZE, // Max total size of elements in the cache
                Integer.MAX_VALUE, // Max entries in the cache
                MAX_MEMORY_CACHE_SIZE / 2, // Max total size of elements in eviction queue
                Integer.MAX_VALUE, // Max length of eviction queue
                Integer.MAX_VALUE)                    // Max cache entry size
        configBuilder.setBitmapMemoryCacheParamsSupplier { bitmapCacheParams }
                .setDecodeFileDescriptorEnabled(true)
                //                .setBitmapsConfig(Bitmap.Config.ARGB_8888) //TODO: if need high picture quality, remove comment here
                //                .setResizeAndRotateEnabledForNetwork(true)
                .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            configBuilder.setMainDiskCacheConfig(
                    DiskCacheConfig.newBuilder(context)
                            .setBaseDirectoryPath(Environment.getExternalStorageDirectory())
                            .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                            .setMaxCacheSize(ImageCore.MAX_DISK_CACHE_SIZE.toLong())
                            .build())
                    .setSmallImageDiskCacheConfig(
                            DiskCacheConfig.newBuilder(context)
                                    .setBaseDirectoryPath(Environment.getExternalStorageDirectory())
                                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                                    .setMaxCacheSize((ImageCore.MAX_DISK_CACHE_SIZE / 2).toLong())
                                    .build())
        } else {
            configBuilder.setMainDiskCacheConfig(
                    DiskCacheConfig.newBuilder(context)
                            .setBaseDirectoryPath(context.applicationContext.cacheDir)
                            .setBaseDirectoryName(IMAGE_PIPELINE_CACHE_DIR)
                            .setMaxCacheSize(ImageCore.MAX_DISK_CACHE_SIZE.toLong())
                            .build())
                    .setSmallImageDiskCacheConfig(
                            DiskCacheConfig.newBuilder(context)
                                    .setBaseDirectoryPath(context.applicationContext.cacheDir)
                                    .setBaseDirectoryName(IMAGE_PIPELINE_SMALL_CACHE_DIR)
                                    .setMaxCacheSize((ImageCore.MAX_DISK_CACHE_SIZE / 2).toLong())
                                    .build())
        }
    }

    private fun configureLoggingListeners(configBuilder: ImagePipelineConfig.Builder) {
        configBuilder.setRequestListeners(Sets.newHashSet(RequestLoggingListener() as RequestListener))
    }

    private fun setImageRequestBuilder(builder: ImageRequestBuilder, priority: Priority?, options: ResizeOptions?, level: ImageRequest.RequestLevel?, persistentCache: Boolean): ImageRequestBuilder {
        builder.isProgressiveRenderingEnabled = true            // 支持渐进式图片
        if (persistentCache) {
            builder.imageType = ImageRequest.ImageType.SMALL
        }
        if (priority != null) {
            builder.requestPriority = priority                  // 设置优先级
        }
        if (options != null) {
            builder.resizeOptions = options
        }
        if (level != null) {
            builder.lowestPermittedRequestLevel = level
        }
        return builder
    }

    private fun createImageRequestWidthUrl(url: String, priority: Priority?, options: ResizeOptions?, level: ImageRequest.RequestLevel?, persistentCache: Boolean): ImageRequest {
        var builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
        builder = setImageRequestBuilder(builder, priority, options, level, persistentCache)
        return builder.build()
    }

    private fun createImageRequestWidthLocalResource(resId: Int): ImageRequest {
        var builder = ImageRequestBuilder.newBuilderWithResourceId(resId)
        builder = setImageRequestBuilder(builder, null, null, ImageRequest.RequestLevel.FULL_FETCH, false)
        return builder.build()
    }

    /**
     * 获取PipelineDraweeControllerBuilder实例，实现对加载图片的控制和定制

     * @param callerContext
     * *
     * @param view
     * *
     * @param listener
     * *
     * @return
     */
    private fun getControllerBuilder(callerContext: Context, view: SimpleDraweeView, listener: GImageLoadListener<ImageInfo>?): PipelineDraweeControllerBuilder {
        val builder = Fresco.newDraweeControllerBuilder()
                .setRetainImageOnFailure(false)
                .setOldController(view.controller)
                .setAutoPlayAnimations(true)
                .setCallerContext(callerContext)
                .setTapToRetryEnabled(false)
        if (listener != null) {
            builder.controllerListener = object : ControllerListener<ImageInfo> {
                override fun onSubmit(id: String, callerContext: Any) {
                    listener.onSubmit(view, id, callerContext)
                }

                override fun onFinalImageSet(id: String, imageInfo: ImageInfo?, animatable: Animatable?) {
                    listener.onFinalImageSet(view, id, imageInfo)
                }

                override fun onIntermediateImageSet(id: String, imageInfo: ImageInfo?) {
                    listener.onIntermediateImageSet(view, id, imageInfo)
                }

                override fun onIntermediateImageFailed(id: String, throwable: Throwable) {
                    listener.onIntermediateImageFailed(view, id, throwable)
                }

                override fun onFailure(id: String, throwable: Throwable) {
                    listener.onFailure(view, id, throwable)
                }

                override fun onRelease(id: String) {
                    listener.onRelease(view, id)
                }
            }
        }
        return builder
    }

    /**
     * 通过资源Id加载图片

     * @param callerContext
     * *
     * @param view
     * *
     * @param resId
     */
    fun load(callerContext: Context, view: SimpleDraweeView, resId: Int) {
        val controllerBuilder = getControllerBuilder(callerContext, view, null)
        val request = createImageRequestWidthLocalResource(resId)
        controllerBuilder.imageRequest = request
        view.controller = controllerBuilder.build()
    }

    /**
     * 加载图片方法，
     * 支持的类型：
     * 远程图片	http://, https://	HttpURLConnection 或者参考 使用其他网络加载方案
     * 本地文件	file://	FileInputStream
     * Content provider	content://	ContentResolver
     * asset目录下的资源	asset://	AssetManager
     * res目录下的资源	res://	Resources.openRawResource

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
     * @param persistentCache
     * *
     * @param onlyLoadCache
     */
    fun load(callerContext: Context, view: SimpleDraweeView, url: String, thumbnailUrl: String?, priority: Priority?, options: ResizeOptions?, level: ImageRequest.RequestLevel?, persistentCache: Boolean, onlyLoadCache: Boolean, listener: GImageLoadListener<ImageInfo>?) {
        var thumbnailRequest: ImageRequest? = null
        if (thumbnailUrl != null && thumbnailUrl.isNotEmpty()) {
            thumbnailRequest = createImageRequestWidthUrl(thumbnailUrl, priority, options, level, persistentCache)
        }
        var request: ImageRequest? = null
        if (!TextUtils.isEmpty(url)) {
            request = createImageRequestWidthUrl(url, priority, options, level, persistentCache)
        }

        val controllerBuilder = getControllerBuilder(callerContext, view, listener)
        if (thumbnailRequest != null) {
            controllerBuilder.lowResImageRequest = thumbnailRequest
        }
        if (request != null) {
            controllerBuilder.imageRequest = request
        }

        view.controller = controllerBuilder.build()
    }

    fun setUndisplayOutOffWifiNetWork(show: Boolean) {
        undisplayOutOffWifiNetWork = show
    }
}