package app.inex.inexfilms.presentation.filmslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import app.inex.inexfilms.core.ui.adapter.BaseAdapter
import app.inex.inexfilms.core.ui.adapter.BaseViewHolder
import app.inex.inexfilms.core.ui.adapter.ConcatAdapter
import app.inex.inexfilms.core.ui.ext.sizableOnFocus
import app.inex.inexfilms.databinding.ItemFilmsSectionBinding
import app.inex.inexfilms.presentation.filmslist.listener.FilmSelectionListener
import app.inex.inexfilms.shared.adapter.films.FilmsAdapter

class FilmsSectionsAdapter(
    private val sectionSelectionListener: SectionSelectionListener,
    private val filmSelectionListener: FilmSelectionListener
): BaseAdapter<FilmsSectionItem, ItemFilmsSectionBinding>() {

    override val viewBindingInflater: (
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ) -> ItemFilmsSectionBinding = ItemFilmsSectionBinding::inflate

    private val BaseViewHolder<ItemFilmsSectionBinding>.filmsAdapter
        get() = binding.filmsRv.adapter as ConcatAdapter

    override fun BaseViewHolder<ItemFilmsSectionBinding>.onCreate() {
        binding.filmsRv.adapter = ConcatAdapter(
            FilmsAdapter(
                filmSelectionListener = filmSelectionListener
            )
        )
    }

    override fun BaseViewHolder<ItemFilmsSectionBinding>.onBind(item: FilmsSectionItem) {
        with(binding) {
            nameTv.text = item.name
            filmsAdapter.setItems(item.films)

            showAllLl.sizableOnFocus()

            showAllLl.setOnClickListener {
                sectionSelectionListener.onSectionSelected(section = item)
            }
        }
    }

    fun interface SectionSelectionListener {
        fun onSectionSelected(section: FilmsSectionItem)
    }
}