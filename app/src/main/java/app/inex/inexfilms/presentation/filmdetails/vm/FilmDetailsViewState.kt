package app.inex.inexfilms.presentation.filmdetails.vm

import app.inex.inexfilms.core.ui.base.BaseViewState
import app.inex.inexfilms.data.models.PlayerPlaylist
import app.inex.inexfilms.data.models.PlayerSource

data class FilmDetailsViewState(
    val isLoading: Boolean,
    val content: Content?,
    val film: PlayerSource?,
    val serial: PlayerPlaylist?,
    val error: String?,
): BaseViewState {

    data class Content(
        val poster: String,
        val name: String
    )

    companion object {
        fun provideInitialState(): FilmDetailsViewState {
            return FilmDetailsViewState(
                isLoading = true,
                content = null,
                film = null,
                serial = null,
                error = null,
            )
        }
    }
}