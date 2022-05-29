package app.inex.inexfilms.presentation.flow.leftmenu

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class LeftNavItem(
    @DrawableRes val icon: Int,
    val text: String,
    val tag: String
)