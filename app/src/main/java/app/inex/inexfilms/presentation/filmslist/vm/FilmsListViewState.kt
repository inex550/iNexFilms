package app.inex.inexfilms.presentation.filmslist.vm

import app.inex.inexfilms.core.ui.base.BaseViewState
import app.inex.inexfilms.presentation.filmslist.adapter.FilmsSectionItem

data class FilmsListViewState(
    val isLoading: Boolean,
    val error: String?,
    val sections: List<FilmsSectionItem>,
    val isSearchLoading: Boolean,
): BaseViewState {

    companion object {
        fun provideInitialState(): FilmsListViewState {
            return FilmsListViewState(
                isLoading = true,
                error = null,
                sections = listOf(),
                isSearchLoading = false,
            )
        }
    }
}