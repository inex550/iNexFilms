package app.inex.inexfilms.presentation.filmslist.vm

import app.inex.inexfilms.core.navigation.Screens
import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.ui.base.BaseViewModel
import app.inex.inexfilms.data.repo.LordFilmRepository
import app.inex.inexfilms.presentation.filmslist.adapter.FilmsSectionItem
import app.inex.inexfilms.shared.adapter.films.FilmItem
import app.inex.inexfilms.shared.adapter.films.FilmItem.Companion.mapToFilmsItem
import app.inex.inexfilms.presentation.filmslist.adapter.FilmsSectionItem.Companion.mapToFilmsSectionItem
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class FilmsListViewModel @AssistedInject constructor(
    private val router: Router,
    private val lordFilmRepository: LordFilmRepository,
    @Assisted private val localRouter: Router,
): BaseViewModel<FilmsListViewState, FilmsListViewAction>(
    initialState = FilmsListViewState.provideInitialState()
) {

    init {
        loadHome()
    }

    override fun provideDefaultError(error: ErrorModel): FilmsListViewAction {
        return FilmsListViewAction.DialogError(error = error)
    }

    private fun loadHome() {
        launchIOCoroutine(
            errorListener = {
                updateState { copy(error = it.errorMessage) }
                true
            },
            finallyListener = { updateState { copy(isLoading = false) } }
        ) {
            val films = lordFilmRepository.loadFilms()

            updateState { copy(sections = films.map { it.mapToFilmsSectionItem() }) }
        }
    }

    fun repeatAfterError() {
        updateState { copy(error = null, isLoading = true) }
        loadHome()
    }

    fun search(query: String) {
        updateState { copy(isSearchLoading = true) }

        launchIOCoroutine(
            finallyListener = { updateState { copy(isSearchLoading = false) } }
        ) {
            val films = lordFilmRepository.searchFilms(query = query)
            performAction(FilmsListViewAction.SearchResult(
                films = films.map { it.mapToFilmsItem() }
            ))
        }
    }

    fun openFilmDetails(film: FilmItem) {
        router.navigateTo(Screens.filmDetailsScreen(filmPage = film.filmPage))
    }

    fun openSectionDetails(section: FilmsSectionItem) {
        localRouter.navigateTo(Screens.sectionDetailsScreen(
            sectionUri = section.uri,
            sectionName = section.name
        ))
    }

    fun onBackPressed() {
        router.exit()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted localRouter: Router
        ): FilmsListViewModel
    }
}