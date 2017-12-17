package com.wuhenzhizao.adapter.example.image

import android.content.Context
import com.wuhenzhizao.titlebar.utils.ScreenUtils

/**
 * Created by liufei on 2017/10/11.
 * wiki地址: http://redmine.gomeo2omx.cn/projects/server/wiki/缩略图服务使用说明
 * 缩略图 URL 格式：http://i-test.meixincdn.com/源图ID.转图参数.后缀名
 * 其中转图参数部分格式为 [宽像素数][x高像素数][c剪取位置][z][q质量]。
 */
object ImageUrlUtils {

    enum class Extension(val value: String) {
        PNG(".png"), JPG(".jpg"), GIF(".gif")
    }

    /**
     * @param context     上下文
     * @param oldUrl      原图地址
     * @param size        新图在屏幕中的比例
     * @param aspectRatio 新图的宽高比
     * @return 处理以后的图片路径
     *
     * * 例子:
     * * 原路径:https://i-pre.meixincdn.com/v1/img/T15aATBCZT1RXrhCrK.jpg
     * * 生成路径:https://i-pre.meixincdn.com/v1/img/T15aATBCZT1RXrhCrK.jpg.540x216.jpg
     * * 原路径无扩展名: https://i-pre.meixincdn.com/v1/img/T15aATBCZT1RXrhCrK
     * * 生成路径: https://i-pre.meixincdn.com/v1/img/T15aATBCZT1RXrhCrK.540x216.jpg
     */
    fun getUrl(context: Context, oldUrl: String, size: ImageWidth, aspectRatio: AspectRatio): String =
            getUrl(context, oldUrl, size, aspectRatio, null)

    /**
     * @param context     上下文
     * *
     * @param oldUrl      原图地址
     * *
     * @param size        新图在屏幕中的比例
     * *
     * @param aspectRatio 新图的宽高比
     * *
     * @param extension   原图路径没有扩展名的时候，需要的扩展名
     * *
     * @return 处理以后的图片路径
     * *
     *
     *
     * * 例子:
     * * 原路径:https://i-pre.meixincdn.com/v1/img/T15aATBCZT1RXrhCrK.jpg
     * * 生成路径:https://i-pre.meixincdn.com/v1/img/T15aATBCZT1RXrhCrK.jpg.540x216.jpg
     * * 原路径无扩展名: https://i-pre.meixincdn.com/v1/img/T15aATBCZT1RXrhCrK
     * * 生成路径: https://i-pre.meixincdn.com/v1/img/T15aATBCZT1RXrhCrK.540x216.gif
     */
    fun getUrl(context: Context, oldUrl: String, size: ImageWidth, aspectRatio: AspectRatio, extension: Extension?): String {
        val screenSize = ScreenUtils.getScreenPixelSize(context)
        val screenWidth = screenSize[0]
        val newImageWidth = getStandardWidth((screenWidth * size.value).toInt())
        var height = ""
        if (aspectRatio !== AspectRatio.UNSPECIFIED) {
            height = "x" + (newImageWidth / aspectRatio.value).toInt()
        }

        var newExtension = getExtension(oldUrl)
        if ("" == newExtension) {
            newExtension = if (extension == null) {
                Extension.JPG.value
            } else {
                extension.value
            }
        }

        return String.format("%s.%s%s%s", oldUrl, newImageWidth, height, newExtension)
    }

    /**
     * 规范宽

     * @param width
     * *
     * @return
     */
    private fun getStandardWidth(width: Int): Int = when {
        width <= 0 -> 60
        width in 0 until 60 -> 60
        width in 60 until 90 -> 90
        width in 90 until 120 -> 120
        width in 120 until 180 -> 180
        width in 180 until 240 -> 240
        width in 240 until 360 -> 360
        width in 360 until 480 -> 480
        width in 480 until 540 -> 540
        width in 540 until 720 -> 720
        width in 720 until 1080 -> 1080
        width > 1080 -> 1080
        else -> width
    }

    /**
     * 通过原url获取扩展名

     * @param oldUrl
     * *
     * @return
     */
    private fun getExtension(oldUrl: String): String = when {
        oldUrl.toLowerCase().endsWith(".png") -> ".png"
        oldUrl.toLowerCase().endsWith(".jpg") -> ".jpg"
        oldUrl.toLowerCase().endsWith(".gif") -> ".gif"
        else -> ""
    }
}