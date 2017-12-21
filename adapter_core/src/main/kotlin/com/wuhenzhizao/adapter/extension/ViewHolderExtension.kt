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
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.*
import com.wuhenzhizao.adapter.interfaces.ViewHolderDelegate

/**
 * Set the text of a TextView.
 *
 * @param viewId The view id.
 * @param value  The text to put in the text view.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setText(@IdRes viewId: Int, value: CharSequence): T {
    val view = get<TextView>(viewId)
    view.text = value
    return this
}

fun <T : ViewHolderDelegate> T.setText(@IdRes viewId: Int, @StringRes strId: Int): T {
    val view = get<TextView>(viewId)
    view.setText(strId)
    return this
}

/**
 * Set text color of a TextView.
 *
 * @param viewId    The view id.
 * @param textColor The text color (not a resource id).
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): T {
    val view = get<TextView>(viewId)
    view.setTextColor(textColor)
    return this
}

/**
 * Set the image of an ImageView from a resource id.
 *
 * @param viewId     The view id.
 * @param imageResId The image resource id.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setImageResource(@IdRes viewId: Int, @DrawableRes imageResId: Int): T {
    val view = get<ImageView>(viewId)
    view.setImageResource(imageResId)
    return this
}

/**
 * Set background color of a view.
 *
 * @param viewId The view id.
 * @param color  A color, not a resource id.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): T {
    val view = get<View>(viewId)
    view.setBackgroundColor(color)
    return this
}

/**
 * Set background of a view.
 *
 * @param viewId        The view id.
 * @param backgroundRes A resource to use as a background.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): T {
    val view = get<View>(viewId)
    view.setBackgroundResource(backgroundRes)
    return this
}

/**
 * Will set the image of an ImageView from a drawable.
 *
 * @param viewId   The view id.
 * @param drawable The image drawable.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setImageDrawable(@IdRes viewId: Int, drawable: Drawable): T {
    val view = get<ImageView>(viewId)
    view.setImageDrawable(drawable)
    return this
}


/**
 * Add an action to set the image of an image view. Can be called multiple times.
 */
fun <T : ViewHolderDelegate> T.setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap): T {
    val view = get<ImageView>(viewId)
    view.setImageBitmap(bitmap)
    return this
}

/**
 * Add an action to set the alpha of a view. Can be called multiple times.
 * Alpha between 0-1.
 */
fun <T : ViewHolderDelegate> T.setAlpha(@IdRes viewId: Int, value: Float): T {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        get<View>(viewId).alpha = value
    } else {
        // Pre-honeycomb hack to set Alpha value
        val alpha = AlphaAnimation(value, value)
        alpha.duration = 0
        alpha.fillAfter = true
        get<View>(viewId).startAnimation(alpha)
    }
    return this
}

/**
 * Set a view visibility to VISIBLE (true) or GONE (false).
 *
 * @param viewId  The view id.
 * @param visible True for VISIBLE, false for GONE.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setGone(@IdRes viewId: Int, visible: Boolean): T {
    val view = get<View>(viewId)
    view.visibility = if (visible) View.VISIBLE else View.GONE
    return this
}

/**
 * Set a view visibility to VISIBLE (true) or INVISIBLE (false).
 *
 * @param viewId  The view id.
 * @param visible True for VISIBLE, false for INVISIBLE.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setVisible(@IdRes viewId: Int, visible: Boolean): T {
    val view = get<View>(viewId)
    view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
    return this
}

/**
 * Add links into a TextView.
 *
 * @param viewId The id of the TextView to linkify.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.linkify(@IdRes viewId: Int): T {
    val view = get<TextView>(viewId)
    Linkify.addLinks(view, Linkify.ALL)
    return this
}

/**
 * Apply the typeface to the given viewId, and enable subpixel rendering.
 */
fun <T : ViewHolderDelegate> T.setTypeface(@IdRes viewId: Int, typeface: Typeface): T {
    val view = get<TextView>(viewId)
    view.typeface = typeface
    view.paintFlags = view.getPaintFlags() or Paint.SUBPIXEL_TEXT_FLAG
    return this
}

/**
 * Apply the typeface to all the given viewIds, and enable subpixel rendering.
 */
fun <T : ViewHolderDelegate> T.setTypeface(typeface: Typeface, vararg viewIds: Int): T {
    for (viewId in viewIds) {
        val view = get<TextView>(viewId)
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
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setProgress(@IdRes viewId: Int, progress: Int): T {
    val view = get<ProgressBar>(viewId)
    view.progress = progress
    return this
}

/**
 * Set the progress and max of a ProgressBar.
 *
 * @param viewId   The view id.
 * @param progress The progress.
 * @param max      The max value of a ProgressBar.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setProgress(@IdRes viewId: Int, progress: Int, max: Int): T {
    val view = get<ProgressBar>(viewId)
    view.max = max
    view.progress = progress
    return this
}

/**
 * Set the range of a ProgressBar to 0...max.
 *
 * @param viewId The view id.
 * @param max    The max value of a ProgressBar.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setMax(@IdRes viewId: Int, max: Int): T {
    val view = get<ProgressBar>(viewId)
    view.max = max
    return this
}

/**
 * Set the rating (the number of stars filled) of a RatingBar.
 *
 * @param viewId The view id.
 * @param rating The rating.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setRating(@IdRes viewId: Int, rating: Float): T {
    val view = get<RatingBar>(viewId)
    view.rating = rating
    return this
}

/**
 * Set the rating (the number of stars filled) and max of a RatingBar.
 *
 * @param viewId The view id.
 * @param rating The rating.
 * @param max    The range of the RatingBar to 0...max.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setRating(@IdRes viewId: Int, rating: Float, max: Int): T {
    val view = get<RatingBar>(viewId)
    view.max = max
    view.rating = rating
    return this
}

/**
 * Set the on click listener of the view.
 *
 * @param viewId   The view id.
 * @param listener The on click listener;
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setOnClickListener(@IdRes viewId: Int, listener: View.OnClickListener): T {
    val view = get<View>(viewId)
    view.setOnClickListener(listener)
    return this
}


/**
 * Set the on touch listener of the view.
 *
 * @param viewId   The view id.
 * @param listener The on touch listener;
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setOnTouchListener(@IdRes viewId: Int, listener: View.OnTouchListener): T {
    val view = get<View>(viewId)
    view.setOnTouchListener(listener)
    return this
}

/**
 * Set the on long click listener of the view.
 *
 * @param viewId   The view id.
 * @param listener The on long click listener;
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setOnLongClickListener(@IdRes viewId: Int, listener: View.OnLongClickListener): T {
    val view = get<View>(viewId)
    view.setOnLongClickListener(listener)
    return this
}

/**
 * Set the on checked change listener of the view.
 *
 * @param viewId   The view id.
 * @param listener The checked change listener of compound button.
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setOnCheckedChangeListener(@IdRes viewId: Int, listener: CompoundButton.OnCheckedChangeListener): T {
    val view = get<CompoundButton>(viewId)
    view.setOnCheckedChangeListener(listener)
    return this
}

/**
 * Set the tag of the view.
 *
 * @param viewId The view id.
 * @param tag    The tag;
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setTag(@IdRes viewId: Int, tag: Any): T {
    val view = get<View>(viewId)
    view.tag = tag
    return this
}

/**
 * Set the tag of the view.
 *
 * @param viewId The view id.
 * @param key    The key of tag;
 * @param tag    The tag;
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setTag(@IdRes viewId: Int, key: Int, tag: Any): T {
    val view = get<View>(viewId)
    view.setTag(key, tag)
    return this
}

/**
 * Set the checked status of a checkable.
 *
 * @param viewId  The view id.
 * @param checked The checked status;
 * @return T: subclass of ViewHolderDelegate.
 */
fun <T : ViewHolderDelegate> T.setChecked(@IdRes viewId: Int, checked: Boolean): T {
    val view = get<View>(viewId)
    // View unable cast to Checkable
    if (view is Checkable) {
        (view as Checkable).isChecked = checked
    }
    return this
}