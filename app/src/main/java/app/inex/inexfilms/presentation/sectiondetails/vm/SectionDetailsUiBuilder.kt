package app.inex.inexfilms.presentation.sectiondetails.vm

import app.inex.inexfilms.core.ui.adapter.BaseItem
import app.inex.inexfilms.shared.adapter.films.FilmItem
import app.inex.inexfilms.shared.adapter.loading.LoadingItem
import javax.inject.Inject

class SectionDetailsUiBuilder @Inject constructor() {

    private val _items: MutableList<BaseItem> = mutableListOf()

    fun getItems(): List<BaseItem> = _items.toList()

    fun setFilms(films: List<FilmItem>) {
        val hasLoading = hasLoading()

        if (hasLoading) removeLoading()
        _items.addAll(films)
        if(hasLoading) setLoading()
    }

    fun setLoading() {
        if (hasLoading().not()) _items.add(LoadingItem)
    }

    fun removeLoading() {
        if (hasLoading()) _items.removeLast()
    }

    fun hasLoading(): Boolean {
        return _items.lastOrNull() is LoadingItem
    }
}