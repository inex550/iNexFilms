package app.inex.inexfilms.presentation.filmslist.listener

import app.inex.inexfilms.shared.adapter.films.FilmItem

fun interface FilmSelectionListener {
    fun onFilmSelected(film: FilmItem)
}