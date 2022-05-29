package app.inex.inexfilms.presentation.filmslist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import app.inex.inexfilms.core.ui.adapter.BaseAdapter
import app.inex.inexfilms.core.ui.adapter.BaseViewHolder
import app.inex.inexfilms.core.ui.ext.getScreenWidth
import app.inex.inexfilms.core.ui.ext.sizableOnFocus
import app.inex.inexfilms.core.ui.ext.updateLayoutParams
import app.inex.inexfilms.databinding.ItemSearchFilmBinding
import app.inex.inexfilms.presentation.filmslist.listener.FilmSelectionListener
import app.inex.inexfilms.shared.adapter.films.FilmItem
import coil.load

class SearchFilmsAdapter(
    private val filmSelectionListener: FilmSelectionListener
): BaseAdapter<FilmItem, ItemSearchFilmBinding>() {

    override val viewBindingInflater: (
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ) -> ItemSearchFilmBinding = ItemSearchFilmBinding::inflate

    override fun BaseViewHolder<ItemSearchFilmBinding>.onBind(item: FilmItem) {
        with(binding) {
            posterIv.load(item.poster)
            nameTv.text = item.name

            root.sizableOnFocus(
                size = FOCUSED_SIZE
            )

//            root.updateLayoutParams {
//                width = getScreenWidth()
//            }

            root.setOnClickListener {
                filmSelectionListener.onFilmSelected(film = item)
            }
        }
    }

    companion object {
        private const val FOCUSED_SIZE: Float = 1.02f
    }
}