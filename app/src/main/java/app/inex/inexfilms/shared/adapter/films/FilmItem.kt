package app.inex.inexfilms.shared.adapter.films

import app.inex.inexfilms.core.ui.adapter.BaseItem
import app.inex.inexfilms.data.models.Film

data class FilmItem(
    val poster: String,
    val name: String,
    val filmPage: String,
): BaseItem {

    companion object {

        fun Film.mapToFilmsItem(): FilmItem {
            return FilmItem(
                poster = poster,
                name = name,
                filmPage = filmPage,
            )
        }
    }
}