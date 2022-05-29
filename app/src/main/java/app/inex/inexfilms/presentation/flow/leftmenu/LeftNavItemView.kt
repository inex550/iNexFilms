package app.inex.inexfilms.presentation.flow.leftmenu

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import app.inex.inexfilms.R
import app.inex.inexfilms.core.ui.ext.dp
import app.inex.inexfilms.core.ui.ext.makeGone
import app.inex.inexfilms.core.ui.ext.updateLayoutParams

class LeftNavItemView(
    context: Context,
    attrs: AttributeSet? = null
): LinearLayout(context, attrs) {

    private var isItemSelected: Boolean = false

    private val iconIv = ImageView(context).apply {
        updateLayoutParams {
            width = SIZE_ICON_IV.dp
            height = SIZE_ICON_IV.dp
        }
    }

    private val textTv = TextView(context, null, R.style.Text_16_B).apply {
        updateLayoutParams {
            width = LayoutParams.WRAP_CONTENT
            height = LayoutParams.WRAP_CONTENT
        }
        makeGone()
    }

    private val space = Space(context).apply {
        updateLayoutParams { width = SIZE_SPACER.dp }
        makeGone()
    }

    fun setTextVisible(isVisible: Boolean) {
        textTv.isVisible = isVisible
        space.isVisible = isVisible
    }

    fun setItemSelected(isSelected: Boolean) {
        isItemSelected = isSelected

        val color = if (isSelected) COLOR_SELECTED else COLOR_NOT_SELECTED

        iconIv.setColorFilter(color)
        textTv.setTextColor(color)
    }

    init {
        setBackgroundResource(R.drawable.bg_ripple_grey_corners_8)

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        isFocusable = true
        isClickable = true

        setPadding(SIZE_PADDING.dp)

        setItemSelected(isSelected = false)

        addView(iconIv)
        addView(space)
        addView(textTv)
    }

    fun setItem(item: LeftNavItem) {
        iconIv.setImageResource(item.icon)
        textTv.text = item.text
    }

    companion object {
        private const val SIZE_ICON_IV = 24
        private const val SIZE_PADDING = 12
        private const val SIZE_SPACER = 16

        private const val COLOR_SELECTED = Color.WHITE
        private const val COLOR_NOT_SELECTED = Color.GRAY
    }
}