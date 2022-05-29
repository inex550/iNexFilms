package app.inex.inexfilms.core.common.ext

import android.webkit.URLUtil
import app.inex.inexfilms.App
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

val String.isHttpUrl: Boolean
    get() = URLUtil.isHttpsUrl(this) || URLUtil.isHttpUrl(this)

fun String.withBaseUrl(): String {
    return if (isHttpUrl) this
    else return App.component.settingsPreferences().lordFilmUrl + if (startsWith("/"))
        this
    else
        "/$this"
}

fun String.parseToDoc(): Document {
    return Jsoup.parse(this)
}