package app.inex.inexfilms.presentation.filmslist.ui

import androidx.core.view.isVisible
import app.inex.inexfilms.App
import app.inex.inexfilms.R
import app.inex.inexfilms.core.common.ext.isNotNull
import app.inex.inexfilms.core.ui.base.BaseMvvmFragment
import app.inex.inexfilms.core.ui.dialog.ErrorDialog
import app.inex.inexfilms.core.ui.ext.*
import app.inex.inexfilms.databinding.FragmentFilmsListBinding
import app.inex.inexfilms.presentation.filmslist.adapter.FilmsSectionItem
import app.inex.inexfilms.presentation.filmslist.adapter.FilmsSectionsAdapter
import app.inex.inexfilms.presentation.filmslist.listener.FilmSelectionListener
import app.inex.inexfilms.presentation.filmslist.vm.FilmsListViewAction
import app.inex.inexfilms.presentation.filmslist.vm.FilmsListViewModel
import app.inex.inexfilms.presentation.filmslist.vm.FilmsListViewState
import javax.inject.Inject

class FilmsListFragment: BaseMvvmFragment<FilmsListViewState, FilmsListViewAction, FilmsListViewModel>(
    layoutResId = R.layout.fragment_films_list
) {

    private val binding by viewBinding(FragmentFilmsListBinding::bind)

    @Inject
    lateinit var viewModelFactory: FilmsListViewModel.Factory

    override val viewModel: FilmsListViewModel by viewModels {
        viewModelFactory.create(localRouter = localRouter)
    }

    private val sectionSelectionListener = FilmsSectionsAdapter.SectionSelectionListener {
        viewModel.openSectionDetails(section = it)
    }

    private val filmSelectionListener = FilmSelectionListener {
        viewModel.openFilmDetails(film = it)
    }

    override fun setupInjection() = App.component.inject(fragment = this)

    private val sectionsAdapter
        get() = binding.sectionsRv.adapter as FilmsSectionsAdapter

    override fun setupUi() {

        with(binding) {
            searchEt.bgOnFocus(
                bgHasFocus = R.drawable.bg_border_color_primary_corners_8,
                bgNotFocus = R.drawable.bg_border_color_grey_corners_8,
            )

            searchEt.setOnEnterClickListener {
                val query: String = binding.searchEt.text.toString()
                viewModel.search(query = query)
            }

            errorFrame.repeatBtn.setOnClickListener {
                viewModel.repeatAfterError()
            }

            sectionsRv.adapter = FilmsSectionsAdapter(
                sectionSelectionListener = sectionSelectionListener,
                filmSelectionListener = filmSelectionListener
            )
        }
    }

    override fun proceedAction(action: FilmsListViewAction) {
        when(action) {
            is FilmsListViewAction.DialogError -> ErrorDialog.newInstance(action.error)
                .show(childFragmentManager, null)

            is FilmsListViewAction.SearchResult -> SearchFilmsDialog(
                films = action.films,
                filmSelectionListener = filmSelectionListener,
            ).show(childFragmentManager, null)
        }
    }

    override fun setupRender() {
        with(viewModel.uiState) {
            renderIn(viewLifecycle, { it.isLoading }, ::renderLoading)
            renderIn(viewLifecycle, { it.error }, ::renderError)
            renderIn(viewLifecycle, { it.sections }, ::renderSections)
            renderIn(viewLifecycle, { it.isSearchLoading }, ::renderSearchLoading)
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        with(binding) {
            loadingFrame.root.isVisible = isLoading
            sectionsRv.isVisible = isLoading.not()
            searchEt.isEnabled = isSearchEnabled()
        }
    }

    private fun renderError(error: String?) {
        with(binding) {
            errorFrame.root.isVisible = error.isNotNull()
            errorFrame.errorTv.text = error.orEmpty()

            searchEt.isEnabled = isSearchEnabled()
        }
    }

    private fun renderSections(sections: List<FilmsSectionItem>) {
        binding.root
        sectionsAdapter.setItems(sections)
    }

    private fun renderSearchLoading(isSearchLoading: Boolean) {
        with(binding) {
            searchLoadingPb.isVisible = isSearchLoading
            searchEt.isEnabled = isSearchEnabled()
        }
    }

    private fun isSearchEnabled(): Boolean {
        return with(uiState) {
            (isLoading || isSearchLoading || error.isNotNull()).not()
        }
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return true
    }
}