package app.inex.inexfilms.core.ui.ext

import android.content.res.Resources.getSystem
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.view.setPadding

private val displayMetrics: DisplayMetrics
    get() = getSystem().displayMetrics

val Int.dp: Int get() = (this * displayMetrics.density).toInt()

val Int.toPx: Int get() = (this / displayMetrics.density).toInt()

fun getScreenWidth(): Int {
    return displayMetrics.widthPixels
}

fun getScreenHeight(): Int {
    return displayMetrics.heightPixels
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeInvisible() {
    visibility = View.INVISIBLE
}

fun View.updateLayoutParams(block: ViewGroup.LayoutParams.() -> Unit) {
    layoutParams = ViewGroup.LayoutParams(width, height).apply(block)
}

fun View.sizableOnFocus(
    size: Float = 1.1f,
    duration: Long = DEFAULT_SIZABLE_DURATION
) {
    setOnFocusChangeListener { v, hasFocus ->
        val scale = if (hasFocus) size else 1f

        v.animate()
            .scaleX(scale).scaleY(scale)
            .setDuration(duration)
            .start()
    }
}

fun View.bgOnFocus(
    @DrawableRes bgHasFocus: Int,
    @DrawableRes bgNotFocus: Int
) {
    setOnFocusChangeListener { v, hasFocus ->
        v.setBackgroundResource(if (hasFocus) bgHasFocus else bgNotFocus)
    }
}

fun EditText.setOnEnterClickListener(listener: () -> Unit) {
    setOnEditorActionListener { _, actionId, event ->
        if (event == null && (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT))
            listener()

        else if (actionId == EditorInfo.IME_NULL && event.action == KeyEvent.ACTION_DOWN)
            listener()

        true
    }
}

private const val DEFAULT_SIZABLE_DURATION = 200L