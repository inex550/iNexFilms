package app.inex.inexfilms.core.network.error

import okhttp3.Response
import java.lang.RuntimeException

class HttpException(response: Response): RuntimeException() {

    val code: Int = response.code
    val status: String = response.message

    override val message: String
        get() = "Error: $code $status"
}