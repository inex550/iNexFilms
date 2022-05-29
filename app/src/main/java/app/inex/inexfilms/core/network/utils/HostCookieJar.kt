package app.inex.inexfilms.core.network.utils

import okhttp3.*

class HostCookieJar: CookieJar {

    private val hostCookies: HashMap<String, List<Cookie>> = hashMapOf()

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return hostCookies[url.host] ?: listOf()
    }

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        hostCookies[url.host] = cookies
    }

//    override fun intercept(chain: Interceptor.Chain): Response {
//        var request = chain.request().newBuilder()
//            .build()
//
//        val hostKey = request.url.host
//
//        if (hostKey in cookies) {
//            request = request.newBuilder()
//                .addHeader("cookie", cookies.getValue(hostKey))
//                .build()
//        }
//
//        val response = chain.proceed(request)
//
//        val newCookies = response.headers["set-cookie"]
//        if (newCookies != null) cookies[hostKey] = newCookies
//
//        return response
//    }
}