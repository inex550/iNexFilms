package app.inex.inexfilms.core.network.ext

import app.inex.inexfilms.core.network.error.HttpException
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

suspend fun Call.await(): Response {
    return suspendCancellableCoroutine { continuation ->
        enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                continuation.resume(response)
            }

            override fun onFailure(call: Call, e: IOException) {
                if (continuation.isCancelled) return
                continuation.resumeWithException(e)
            }
        })

        continuation.invokeOnCancellation {
            cancel()
        }
    }
}

fun Response.okBody(): String {
    if (code in 200..299)
        return body?.string().orEmpty()

    throw HttpException(this)
}

suspend fun OkHttpClient.get(request: Request.Builder): String {
    request.get()
    return newCall(request.build()).await().okBody()
}

suspend fun OkHttpClient.post(request: Request.Builder, body: RequestBody): String {
    request.post(body)
    return newCall(request.build()).await().okBody()
}