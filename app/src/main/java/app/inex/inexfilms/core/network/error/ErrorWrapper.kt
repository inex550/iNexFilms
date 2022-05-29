package app.inex.inexfilms.core.network.error

sealed class ErrorWrapper {

    data class CustomError(val code: Int, val message: String): ErrorWrapper()
    object NetworkError: ErrorWrapper()
    object TimeoutError: ErrorWrapper()
    object UnknownError: ErrorWrapper()
}
