package app.inex.inexfilms.presentation.filmdetails.vm

import app.inex.inexfilms.core.common.ext.isNotNull
import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.ui.base.BaseViewModel
import app.inex.inexfilms.data.repo.LordFilmRepository
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FilmDetailsViewModel @AssistedInject constructor(
    private val router: Router,
    private val repository: LordFilmRepository,
    @Assisted private val filmPage: String
): BaseViewModel<FilmDetailsViewState, FilmDetailsViewAction>(
    initialState = FilmDetailsViewState.provideInitialState()
) {

    init {
        loadFilmDetail()
    }

    override fun provideDefaultError(error: ErrorModel): FilmDetailsViewAction {
        return FilmDetailsViewAction.DialogError(error = error)
    }

    private fun loadFilmDetail() {
        launchIOCoroutine(
            errorListener = {
                updateState { copy(error = it.message) }
                true
            },
            finallyListener = { updateState { copy(isLoading = false) } }
        ) {
            val filmDetails = repository.loadFilmPage(filmPage = filmPage)

            updateState { copy(content = FilmDetailsViewState.Content(
                poster = filmDetails.poster,
                name = filmDetails.name,
            )) }

            loadPlayerInfo(playerUrl = filmDetails.playerUrl)
        }
    }

    private fun loadPlayerInfo(playerUrl: String) {
        launchIOCoroutine {
            val playerInfo = repository.loadPlayerInfo(playerUrl = playerUrl)

            if (playerInfo.film.isNotNull())
                updateState { copy(film = playerInfo.film) }

            else if (playerInfo.serial.isNotNull())
                updateState { copy(serial = playerInfo.serial) }
        }
    }

    fun onBackPressed() {
        router.exit()
    }

    fun refresh() {
        updateState { FilmDetailsViewState.provideInitialState() }
        loadFilmDetail()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted filmPage: String
        ): FilmDetailsViewModel
    }
}