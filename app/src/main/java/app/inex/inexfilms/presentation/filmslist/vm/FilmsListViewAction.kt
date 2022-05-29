package app.inex.inexfilms.presentation.filmslist.vm

import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.ui.base.BaseViewAction
import app.inex.inexfilms.shared.adapter.films.FilmItem

sealed class FilmsListViewAction: BaseViewAction {

    data class DialogError(val error: ErrorModel): FilmsListViewAction()

    data class SearchResult(val films: List<FilmItem>): FilmsListViewAction()
}
