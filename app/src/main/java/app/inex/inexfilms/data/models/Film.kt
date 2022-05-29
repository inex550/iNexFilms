package app.inex.inexfilms.data.models

data class Film(
    val poster: String,
    val name: String,
    val filmPage: String,
    val year: String? = null,
)