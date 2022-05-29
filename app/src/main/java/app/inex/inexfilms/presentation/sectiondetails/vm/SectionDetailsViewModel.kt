package app.inex.inexfilms.presentation.sectiondetails.vm

import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.ui.base.BaseViewModel
import app.inex.inexfilms.data.repo.LordFilmRepository
import app.inex.inexfilms.shared.adapter.films.FilmItem.Companion.mapToFilmsItem
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SectionDetailsViewModel @AssistedInject constructor(
    private val repository: LordFilmRepository,
    private val uiBuilder: SectionDetailsUiBuilder,
    @Assisted private val sectionUri: String,
    @Assisted private val router: Router,
): BaseViewModel<SectionDetailsViewState, SectionDetailsViewAction>(
    initialState = SectionDetailsViewState.provideInitialState()
) {
    init {
        loadNextPage()
    }

    private var pageNum = FIRST_PAGE

    var isPageLoading: Boolean = false
        private set

    var lastPageLoaded: Boolean = false
        private set

    override fun provideDefaultError(error: ErrorModel): SectionDetailsViewAction {
        return SectionDetailsViewAction.DialogError(error)
    }

    fun loadNextPage() {
        launchIOCoroutine(
            errorListener = loadNextPageErrorListener(),
            finallyListener = {
                isPageLoading = false
                updateState { copy(isLoading = false) }
            }
        ) {
            isPageLoading = true

            val films = repository.loadNextSectionPageFilms(sectionUri, if (pageNum == 1) null else pageNum)

            uiBuilder.setFilms(films.map { it.mapToFilmsItem() })
            uiBuilder.setLoading()

            updateState { copy(items = uiBuilder.getItems()) }

            pageNum += 1
        }
    }

    private fun loadNextPageErrorListener() = ErrorListener {
        if (it.code == 404) {
            uiBuilder.removeLoading()
            updateState { copy(items = uiBuilder.getItems()) }

            lastPageLoaded = true

            return@ErrorListener false
        }

        if (uiState.value.isLoading) {
            updateState { copy(error = it.errorMessage) }
        }

        true
    }

    fun onBackPressed() {
        router.exit()
    }

    fun refresh() {
        pageNum = FIRST_PAGE

        updateState { SectionDetailsViewState.provideInitialState() }
        loadNextPage()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted sectionUri: String,
            @Assisted router: Router,
        ): SectionDetailsViewModel
    }


    companion object {
        const val FIRST_PAGE = 1
    }
}