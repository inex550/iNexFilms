package app.inex.inexfilms.core.common.utils

fun <T> uiLazy(block: () -> T) = lazy(LazyThreadSafetyMode.NONE, block)