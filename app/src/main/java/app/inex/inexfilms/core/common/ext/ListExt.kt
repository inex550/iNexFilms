package app.inex.inexfilms.core.common.ext

fun <T> List<T>.indexOfMaxBy(block: (T) -> Int): Int {
    var maxIdx = 0
    forEachIndexed { index, item ->
        if (block(item) > block(this[maxIdx]))
            maxIdx = index
    }
    return maxIdx
}

fun <T> List<T>.maxBy(block: (T) -> Int): T {
    var max = get(0)
    forEach { item ->
        if (block(item) > block(max))
            max = item
    }
    return max;
}