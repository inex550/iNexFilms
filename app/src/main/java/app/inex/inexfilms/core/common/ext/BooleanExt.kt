package app.inex.inexfilms.core.common.ext

fun Boolean?.isTrue() = this == true

fun Boolean?.isFalse() = this == false

fun Boolean?.orTrue() = this ?: true

fun Boolean?.orFalse() = this ?: false