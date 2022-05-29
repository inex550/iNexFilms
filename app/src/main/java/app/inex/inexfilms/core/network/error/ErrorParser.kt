package app.inex.inexfilms.core.network.error

import app.inex.inexfilms.App
import app.inex.inexfilms.R
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

object ErrorParser {

    fun parseThrowable(t: Throwable): ErrorModel {
        return when(t) {
            is HttpException -> ErrorModel(t.code, t.status)
            is TimeoutException -> ErrorModel(null, App.context.getString(R.string.timeout_error))
            is SSLHandshakeException -> ErrorModel(null, App.context.getString(R.string.ssl_handshake_error))
            is NotImplementedError -> ErrorModel(666, App.context.getString(R.string.my_bad_error))
            else -> ErrorModel(null, App.context.getString(R.string.unknown_error))
        }
    }
}