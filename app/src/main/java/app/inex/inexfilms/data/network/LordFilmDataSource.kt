package app.inex.inexfilms.data.network

import app.inex.inexfilms.core.common.ext.withBaseUrl
import app.inex.inexfilms.core.network.ext.get
import app.inex.inexfilms.core.network.ext.post
import app.inex.inexfilms.core.prefs.SettingsPreferences
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class LordFilmDataSource @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val prefs: SettingsPreferences,
) {

    suspend fun loadHome(): String {
        val request = Request.Builder()
            .url(prefs.lordFilmUrl + PAGE_HOME)

        return okHttpClient.get(request)
    }

    suspend fun search(query: String): String {
        val request = Request.Builder()
            .url(prefs.lordFilmUrl + PAGE_HOME)

        val requestBody = FormBody.Builder()
            .add("do", "search")
            .add("subaction", "search")
            .add("story", query)
            .build()

        return okHttpClient.post(request, requestBody)
    }

    suspend fun loadPage(page: String): String {
        val request = Request.Builder()
            .url(page.withBaseUrl())

        return okHttpClient.get(request)
    }

    companion object {
        private const val PAGE_HOME = "/"
    }
}