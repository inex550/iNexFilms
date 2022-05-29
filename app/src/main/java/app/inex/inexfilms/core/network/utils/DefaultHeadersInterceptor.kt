package app.inex.inexfilms.core.network.utils

import okhttp3.Interceptor
import okhttp3.Response

class DefaultHeadersInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Referer", DEFAULT_REFERER)
            .build()

        return chain.proceed(request)
    }

    companion object {
        private const val DEFAULT_REFERER: String = "https://google.com/"
    }
}