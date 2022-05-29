package app.inex.inexfilms.shared.adapter.films

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import app.inex.inexfilms.core.common.ext.withBaseUrl
import app.inex.inexfilms.core.ui.adapter.BaseDelegateAdapter
import app.inex.inexfilms.core.ui.adapter.BaseItem
import app.inex.inexfilms.core.ui.adapter.BaseViewHolder
import app.inex.inexfilms.core.ui.ext.dp
import app.inex.inexfilms.core.ui.ext.sizableOnFocus
import app.inex.inexfilms.databinding.ItemFilmBinding
import app.inex.inexfilms.presentation.filmslist.listener.FilmSelectionListener
import coil.load

class FilmsAdapter(
    private val filmSelectionListener: FilmSelectionListener
): BaseDelegateAdapter<ItemFilmBinding, FilmItem>() {

    override val viewBindingInflater: (
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ) -> ItemFilmBinding = ItemFilmBinding::inflate


    override fun BaseViewHolder<ItemFilmBinding>.onCreate() {
        binding.root.updateLayoutParams {
            width = ITEM_WIDTH.dp
        }
    }

    override fun BaseViewHolder<ItemFilmBinding>.onBind(item: FilmItem) {
        with(binding) {
            nameTv.text = item.name

            posterIv.load(item.poster.withBaseUrl()) {
                crossfade(true)
                error(ColorDrawable(Color.GRAY))
                placeholder(ColorDrawable(Color.GRAY))
            }

            root.sizableOnFocus()

            root.setOnClickListener {
                filmSelectionListener.onFilmSelected(film = item)
            }
        }
    }

    override fun areItemsTheSame(old: FilmItem, new: FilmItem): Boolean {
        return old.filmPage == new.filmPage
    }

    override fun areContentsTheSame(old: FilmItem, new: FilmItem): Boolean {
        return old.name == new.name
            && old.poster == new.poster
    }

    override fun isForItem(item: BaseItem): Boolean {
        return item is FilmItem
    }

    companion object {
        const val ITEM_WIDTH = 120
    }
}
