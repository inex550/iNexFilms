package app.inex.inexfilms.presentation.filmslist.adapter

import app.inex.inexfilms.core.ui.adapter.BaseItem
import app.inex.inexfilms.data.models.FilmsSection
import app.inex.inexfilms.shared.adapter.films.FilmItem
import app.inex.inexfilms.shared.adapter.films.FilmItem.Companion.mapToFilmsItem

data class FilmsSectionItem(
    val name: String,
    val uri: String,
    val films: List<FilmItem>
): BaseItem {

    companion object {
        fun FilmsSection.mapToFilmsSectionItem(): FilmsSectionItem {
            return FilmsSectionItem(
                name = name,
                uri = uri,
                films = films.map { it.mapToFilmsItem() }
            )
        }
    }
}