package app.inex.inexfilms.presentation.filmslist.ui

import androidx.core.view.isVisible
import app.inex.inexfilms.R
import app.inex.inexfilms.core.ui.base.BaseDialogFragment
import app.inex.inexfilms.core.ui.ext.viewBinding
import app.inex.inexfilms.databinding.DialogSearchFilmsBinding
import app.inex.inexfilms.shared.adapter.films.FilmItem
import app.inex.inexfilms.presentation.filmslist.adapter.SearchFilmsAdapter
import app.inex.inexfilms.presentation.filmslist.listener.FilmSelectionListener

class SearchFilmsDialog(
    private val films: List<FilmItem>,
    private val filmSelectionListener: FilmSelectionListener,
): BaseDialogFragment(
    layoutResId = R.layout.dialog_search_films
) {

    private val binding by viewBinding(DialogSearchFilmsBinding::bind)

    private val searchFilmsAdapter
        get() = binding.searchFilmsRv.adapter as SearchFilmsAdapter

    override fun setupUi() {
        with(binding) {
            searchFilmsRv.adapter = SearchFilmsAdapter(
                filmSelectionListener = filmSelectionListener
            )

            searchFilmsAdapter.setItems(films)

            notFoundTv.isVisible = films.isEmpty()

            closeBtn.setOnClickListener {
                dismiss()
            }
        }
    }
}