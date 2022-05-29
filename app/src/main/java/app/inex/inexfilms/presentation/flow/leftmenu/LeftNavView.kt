package app.inex.inexfilms.presentation.flow.leftmenu

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.Space
import androidx.core.view.setPadding
import app.inex.inexfilms.core.ui.ext.dp
import app.inex.inexfilms.core.ui.ext.updateLayoutParams

class LeftNavView(
    context: Context,
    attrs: AttributeSet
): LinearLayout(context, attrs) {

    private var items: List<LeftNavItem> = emptyList()
    private var itemViews: List<LeftNavItemView> = emptyList()

    private var onItemSelectedListener: OnItemSelectedListener? = null

    fun setOnItemSelectedListener(listener: OnItemSelectedListener) {
        onItemSelectedListener = listener
    }

    fun setItems(items: List<LeftNavItem>) {
        orientation = VERTICAL
        gravity = Gravity.CENTER_VERTICAL

        setPadding(size = SIZE_PADDING)

        this.items = items

        itemViews = items.map { item ->
            LeftNavItemView(context).apply {
                setItem(item)
            }
        }

        itemViews.forEachIndexed { index, itemView ->
            val item = items[index]

            itemView.updateLayoutParams {
                width = LayoutParams.WRAP_CONTENT
                height = LayoutParams.WRAP_CONTENT
            }
            itemView.setItem(item)
            addView(itemView)

            setItemFocusableListener(itemView)
            itemView.setOnClickListener(itemClickListener)

            if (index != itemViews.lastIndex) {
                addView(Space(context).apply {
                    updateLayoutParams { height = SIZE_DIVIDER.dp }
                })
            }
        }

        selectItemByPosition(position = 0)
    }

    private fun makeOpen(isOpen: Boolean) {
        itemViews.forEach { it.setTextVisible(isOpen) }
    }

    private var anyHasFocus: Boolean = false

    private fun setItemFocusableListener(item: LeftNavItemView) {
        item.setOnFocusChangeListener { _, _ ->
            val newAnyHasFocus = itemViews.any { it.isFocused }

            if (newAnyHasFocus != anyHasFocus)
                makeOpen(newAnyHasFocus)

            anyHasFocus = newAnyHasFocus
        }
    }

    fun selectItemByPosition(position: Int) {
        itemViews.forEach { it.setItemSelected(isSelected = false) }
        itemViews[position].setItemSelected(isSelected = true)
        onItemSelectedListener?.onItemSelected(item = items[position])
    }

    fun selectItemByTag(tag: String) {
        val itemPosition = items.indexOfFirst { it.tag == tag }
        selectItemByPosition(itemPosition)
    }

    private val itemClickListener = OnClickListener { clickedView ->
        val itemPosition = itemViews.indexOf(clickedView)
        selectItemByPosition(itemPosition)
    }

    fun interface OnItemSelectedListener {
        fun onItemSelected(item: LeftNavItem)
    }

    companion object {
        private const val SIZE_PADDING = 12
        private const val SIZE_DIVIDER = 12
    }
}