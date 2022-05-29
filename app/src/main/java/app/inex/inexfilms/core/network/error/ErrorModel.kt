package app.inex.inexfilms.core.network.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorModel(
    val code: Int? = null,
    val message: String,
): Parcelable {

    val errorMessage: String get() = message.ifEmpty { code?.toString() ?: "" }
}