package app.inex.inexfilms.presentation.sectiondetails.ui

import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import app.inex.inexfilms.App
import app.inex.inexfilms.R
import app.inex.inexfilms.core.common.ext.isNotNull
import app.inex.inexfilms.core.common.utils.uiLazy
import app.inex.inexfilms.core.navigation.Screens
import app.inex.inexfilms.core.ui.adapter.BaseItem
import app.inex.inexfilms.core.ui.adapter.ConcatAdapter
import app.inex.inexfilms.core.ui.adapter.PaginationListener
import app.inex.inexfilms.core.ui.base.BaseMvvmFragment
import app.inex.inexfilms.core.ui.dialog.ErrorDialog
import app.inex.inexfilms.core.ui.ext.*
import app.inex.inexfilms.databinding.FragmentSectionDetailsBinding
import app.inex.inexfilms.presentation.sectiondetails.vm.SectionDetailsViewAction
import app.inex.inexfilms.presentation.sectiondetails.vm.SectionDetailsViewModel
import app.inex.inexfilms.presentation.sectiondetails.vm.SectionDetailsViewState
import app.inex.inexfilms.shared.adapter.films.FilmsAdapter
import app.inex.inexfilms.shared.adapter.loading.LoadingAdapter
import app.inex.inexfilms.shared.adapter.loading.LoadingItem
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class SectionDetailsFragment: BaseMvvmFragment<SectionDetailsViewState, SectionDetailsViewAction, SectionDetailsViewModel>(
    layoutResId = R.layout.fragment_section_details
) {
    private val binding by viewBinding(FragmentSectionDetailsBinding::bind)

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var viewModelFactory: SectionDetailsViewModel.Factory

    override val viewModel: SectionDetailsViewModel by viewModels {
        viewModelFactory.create(router = localRouter, sectionUri = sectionUri)
    }

    private val sectionUri: String by uiLazy {
        requireArguments().getString(ARG_SECTION_URI).orEmpty()
    }

    private val sectionName: String by uiLazy {
        requireArguments().getString(ARG_SECTION_NAME).orEmpty()
    }

    private val filmsAdapter
        get() = binding.filmsRv.adapter as ConcatAdapter

    override fun setupInjection() = App.component.inject(fragment = this)

    override fun setupUi() {
        with(binding) {
            toolbar.setNavigationOnClickListener {
                onBackPressed()
            }

            sectionNameTv.text = sectionName

            filmsRv.layoutManager = GridLayoutManager(requireContext(), DEFAULT_SPAN_COUNT).apply {
                spanSizeLookup = spanSizeLookup()
            }

            filmsRv.adapter = ConcatAdapter(
                FilmsAdapter {
                    router.navigateTo(Screens.filmDetailsScreen(it.filmPage))
                },
                LoadingAdapter()
            )

            errorFrame.repeatBtn.setOnClickListener {
                viewModel.refresh()
            }

            filmsRv.addOnScrollListener(paginationListener())
        }
    }

    private fun paginationListener() = object : PaginationListener(binding.filmsRv.layoutManager as LinearLayoutManager) {
        override fun loadNextPage() {
            viewModel.loadNextPage()
        }

        override fun isLastPage(): Boolean {
            return viewModel.lastPageLoaded
        }

        override fun isLoading(): Boolean {
            return viewModel.isPageLoading
        }
    }

    private fun spanSizeLookup() = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            return if (filmsAdapter.getItems().getOrNull(position) is LoadingItem)
                DEFAULT_SPAN_COUNT
            else 1
        }
    }

    override fun proceedAction(action: SectionDetailsViewAction) {
        when(action) {
            is SectionDetailsViewAction.DialogError -> ErrorDialog.newInstance(errorModel = action.error)
                .show(childFragmentManager, null)
        }
    }

    override fun setupRender() {
        with(viewModel.uiState) {
            renderIn(lifecycle, { it.isLoading }, ::renderLoading)
            renderIn(lifecycle, { it.items }, ::renderItems)
            renderIn(lifecycle, { it.error }, ::renderError)
        }
    }

    private fun renderLoading(isLoading: Boolean) {
        with(binding) {
            loadingFrame.root.isVisible = isLoading
        }
    }

    private fun renderItems(items: List<BaseItem>) {
        filmsAdapter.setItems(items)
    }

    private fun renderError(error: String?) {
        with(binding) {
            errorFrame.root.isVisible = error.isNotNull()
            errorFrame.errorTv.text = error.orEmpty()
        }
    }

    override fun onBackPressed(): Boolean {
        viewModel.onBackPressed()
        return true
    }

    companion object {
        fun newInstance(sectionUri: String, sectionName: String): SectionDetailsFragment = SectionDetailsFragment().withArgs {
            putString(ARG_SECTION_URI, sectionUri)
            putString(ARG_SECTION_NAME, sectionName)
        }

        private const val ARG_SECTION_URI = "ARG_SECTION_URI"
        private const val ARG_SECTION_NAME = "ARG_SECTION_NAME"

        private const val DEFAULT_SPAN_COUNT = 5
    }
}