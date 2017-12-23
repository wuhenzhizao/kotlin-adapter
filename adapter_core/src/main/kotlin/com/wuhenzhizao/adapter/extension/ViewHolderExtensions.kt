package com.wuhenzhizao.adapter.extension

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.text.util.Linkify
import android.view.MotionEvent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import com.wuhenzhizao.adapter.interfaces.ViewHolderSupport

/**************************************************************************
 *                                                                        *
 *                实现ViewHolder对视图操作的链式调用                          *
 *                                                                        *
 **************************************************************************/

/**
 * Set the text of a TextView.
 *
 * @param viewId The view id.
 * @param value  The text to put in the text view.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setText(@IdRes viewId: Int, value: CharSequence): T {
    val view = getView<TextView>(viewId)
    view.text = value
    return this
}

fun <T : ViewHolderSupport> T.setText(@IdRes viewId: Int, @StringRes strId: Int): T {
    val view = getView<TextView>(viewId)
    view.setText(strId)
    return this
}

/**
 * Set text color of a TextView.
 *
 * @param viewId    The view id.
 * @param textColor The text color (not a resource id).
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): T {
    val view = getView<TextView>(viewId)
    view.setTextColor(textColor)
    return this
}

/**
 * Set the image of an ImageView from a resource id.
 *
 * @param viewId     The view id.
 * @param imageResId The image resource id.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): T {
    val view = getView<ImageView>(viewId)
    view.setImageResource(imageResId)
    return this
}

/**
 * Set the image of an ImageView from a resource id.
 *
 * @param viewId     The view id.
 * @param imageResId The image resource id.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.displayImageUrl(@IdRes viewId: Int, block: (imageView: ImageView) -> Unit): T {
    val view = getView<ImageView>(viewId)
    block(view)
    return this
}

/**
 * Set background color of a view.
 *
 * @param viewId The view id.
 * @param color  A color, not a resource id.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): T {
    val view = getView<View>(viewId)
    view.setBackgroundColor(color)
    return this
}

/**
 * Set background of a view.
 *
 * @param viewId        The view id.
 * @param backgroundRes A resource to use as a background.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): T {
    val view = getView<View>(viewId)
    view.setBackgroundResource(backgroundRes)
    return this
}

/**
 * Set the image of an ImageView from a drawable.
 *
 * @param viewId   The view id.
 * @param drawable The image drawable.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setImageDrawable(@IdRes viewId: Int, drawable: Drawable): T {
    val view = getView<ImageView>(viewId)
    view.setImageDrawable(drawable)
    return this
}


/**
 * Add an action to set the image of an image view. Can be called multiple times.
 */
fun <T : ViewHolderSupport> T.setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): T {
    val view = getView<ImageView>(viewId)
    view.setImageBitmap(bitmap)
    return this
}

/**
 * Add an action to set the alpha of a view. Can be called multiple times.
 * Alpha between 0-1.
 */
fun <T : ViewHolderSupport> T.setAlpha(@IdRes viewId: Int, value: Float): T {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        getView<View>(viewId).alpha = value
    } else {
        // Pre-honeycomb hack to set Alpha value
        val alpha = AlphaAnimation(value, value)
        alpha.duration = 0
        alpha.fillAfter = true
        getView<View>(viewId).startAnimation(alpha)
    }
    return this
}

/**
 * Set a view visibility to VISIBLE (true) or GONE (false).
 *
 * @param viewId  The view id.
 * @param visible True for VISIBLE, false for GONE.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setGone(@IdRes viewId: Int, visible: Boolean): T {
    val view = getView<View>(viewId)
    view.visibility = if (visible) View.VISIBLE else View.GONE
    return this
}

/**
 * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
 *
 * @param viewId  The view id.
 * @param visible True for VISIBLE, false for INVISIBLE.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setVisible(@IdRes viewId: Int, visible: Boolean): T {
    val view = getView<View>(viewId)
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    return this
}

/**
 * Add links into a TextView.
 *
 * @param viewId The id of the TextView to linkify.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.linkify(@IdRes viewId: Int): T {
    val view = getView<TextView>(viewId)
    Linkify.addLinks(view, Linkify.ALL)
    return this
}

/**
 * Apply the typeface to the given viewId, and enable subpixel rendering.
 */
fun <T : ViewHolderSupport> T.setTypeface(@IdRes viewId: Int, typeface: Typeface): T {
    val view = getView<TextView>(viewId)
    view.typeface = typeface
    view.paintFlags = view.getPaintFlags() or Paint.SUBPIXEL_TEXT_FLAG
    return this
}

/**
 * Apply the typeface to all the given viewIds, and enable subpixel rendering.
 */
fun <T : ViewHolderSupport> T.setTypeface(typeface: Typeface, vararg viewIds: Int): T {
    for (viewId in viewIds) {
        val view = getView<TextView>(viewId)
        view.typeface = typeface
        view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
    }
    return this
}

/**
 * Set the progress of a ProgressBar.
 *
 * @param viewId   The view id.
 * @param progress The progress.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setProgress(@IdRes viewId: Int, progress: Int): T {
    val view = getView<ProgressBar>(viewId)
    view.progress = progress
    return this
}

/**
 * Set the progress and max of a ProgressBar.
 *
 * @param viewId   The view id.
 * @param progress The progress.
 * @param max      The max value of a ProgressBar.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setProgress(@IdRes viewId: Int, progress: Int, max: Int): T {
    val view = getView<ProgressBar>(viewId)
    view.max = max
    view.progress = progress
    return this
}

/**
 * Set the range of a ProgressBar to 0...max.
 *
 * @param viewId The view id.
 * @param max    The max value of a ProgressBar.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setMax(@IdRes viewId: Int, max: Int): T {
    val view = getView<ProgressBar>(viewId)
    view.max = max
    return this
}

/**
 * Set the rating (the number of stars filled) of a RatingBar.
 *
 * @param viewId The view id.
 * @param rating The rating.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setRating(@IdRes viewId: Int, rating: Float): T {
    val view = getView<RatingBar>(viewId)
    view.rating = rating
    return this
}

/**
 * Set the rating (the number of stars filled) and max of a RatingBar.
 *
 * @param viewId The view id.
 * @param rating The rating.
 * @param max    The range of the RatingBar to 0...max.
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setRating(@IdRes viewId: Int, rating: Float, max: Int): T {
    val view = getView<RatingBar>(viewId)
    view.max = max
    view.rating = rating
    return this
}

/**
 * Set the on click listener of the view.
 *
 * @param viewId   The view id.
 * @param block
 * @return T: subclass of ViewHolderSupport.
 */
inline fun <T : ViewHolderSupport> T.setOnClickListener(@IdRes viewId: Int, crossinline block: (v: View) -> Unit): T {
    val view = getView<View>(viewId)
    view.setOnClickListener {
        block(it)
    }
    return this
}


/**
 * Set the on touch listener of the view.
 *
 * @param viewId   The view id.
 * @param block
 * @return T: subclass of ViewHolderSupport.
 */
inline fun <T : ViewHolderSupport> T.setOnTouchListener(
        @IdRes viewId: Int, crossinline block: (v: View, event: MotionEvent) -> Boolean): T {
    val view = getView<View>(viewId)
    view.setOnTouchListener { v, event ->
        return@setOnTouchListener block(v, event)
    }
    return this
}

/**
 * Set the on long click listener of the view.
 *
 * @param viewId   The view id.
 * @param listener The on long click listener;
 * @return T: subclass of ViewHolderSupport.
 */
inline fun <T : ViewHolderSupport> T.setOnLongClickListener(
        @IdRes viewId: Int, crossinline block: (v: View) -> Boolean): T {
    val view = getView<View>(viewId)
    view.setOnLongClickListener {
        return@setOnLongClickListener block(it)
    }
    return this
}

/**
 * Set the on checked change listener of the view.
 *
 * @param viewId   The view id.
 * @param listener The checked change listener of compound button.
 * @return T: subclass of ViewHolderSupport.
 */
inline fun <T : ViewHolderSupport> T.setOnCheckedChangeListener(
        @IdRes viewId: Int, crossinline block: (buttonView: CompoundButton, isChecked: Boolean) -> Unit): T {
    val view = getView<CompoundButton>(viewId)
    view.setOnCheckedChangeListener { buttonView, isChecked ->
        block(buttonView, isChecked)
    }
    return this
}

/**
 * Set the tag of the view.
 *
 * @param viewId The view id.
 * @param tag    The tag;
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setTag(@IdRes viewId: Int, tag: Any): T {
    val view = getView<View>(viewId)
    view.tag = tag
    return this
}

/**
 * Set the tag of the view.
 *
 * @param viewId The view id.
 * @param key    The key of tag;
 * @param tag    The tag;
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setTag(@IdRes viewId: Int, key: Int, tag: Any): T {
    val view = getView<View>(viewId)
    view.setTag(key, tag)
    return this
}

/**
 * Set the checked status of a checkable.
 *
 * @param viewId  The view id.
 * @param checked The checked status;
 * @return T: subclass of ViewHolderSupport.
 */
fun <T : ViewHolderSupport> T.setChecked(@IdRes viewId: Int, checked: Boolean): T {
    val view = getView<View>(viewId)
    // View unable cast to Checkable
    if (view is Checkable) {
        (view as Checkable).isChecked = checked
    }
    return this
}