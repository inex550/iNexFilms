package app.inex.inexfilms.data.models

data class FilmsSection(
    val name: String,
    val uri: String,
    val films: List<Film>,
)