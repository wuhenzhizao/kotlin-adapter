package com.gome.common.image

import com.facebook.drawee.view.SimpleDraweeView

/**
 * Created by liufei on 2017/10/11.
 */
abstract class GImageLoadListener<in INFO> {
    /**
     * Called before the image request is submitted.
     *
     *  IMPORTANT: It is not safe to reuse the controller from within this callback!

     * @param imageView
     * *
     * @param id            controller id
     * *
     * @param callerContext caller context
     */
    fun onSubmit(imageView: SimpleDraweeView, id: String, callerContext: Any) {}

    /**
     * Called after the final image has been set.

     * @param imageView
     * *
     * @param id        controller id
     * *
     * @param imageInfo image info
     * *
     * @param imageView
     */
    abstract fun onFinalImageSet(imageView: SimpleDraweeView, id: String, imageInfo: INFO?)

    /**
     * Called after any intermediate image has been set.

     * @param imageView
     * *
     * @param id        controller id
     * *
     * @param imageInfo image info
     */
    fun onIntermediateImageSet(imageView: SimpleDraweeView, id: String, imageInfo: INFO?) {}

    /**
     * Called after the fetch of the intermediate image failed.

     * @param imageView
     * *
     * @param id        controller id
     * *
     * @param throwable failure cause
     */
    fun onIntermediateImageFailed(imageView: SimpleDraweeView, id: String, throwable: Throwable) {}

    /**
     * Called after the fetch of the final image failed.

     * @param imageView
     * *
     * @param id        controller id
     * *
     * @param throwable failure cause
     */
    fun onFailure(imageView: SimpleDraweeView, id: String, throwable: Throwable) {}

    /**
     * Called after the controller released the fetched image.
     *
     *  IMPORTANT: It is not safe to reuse the controller from within this callback!

     * @param imageView
     * *
     * @param id controller id
     */
    fun onRelease(imageView: SimpleDraweeView, id: String) {}
}