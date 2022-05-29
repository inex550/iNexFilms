package app.inex.inexfilms.shared.adapter.loading

import android.view.LayoutInflater
import android.view.ViewGroup
import app.inex.inexfilms.core.ui.adapter.BaseDelegateAdapter
import app.inex.inexfilms.core.ui.adapter.BaseItem
import app.inex.inexfilms.databinding.ItemLoadingBinding

class LoadingAdapter: BaseDelegateAdapter<ItemLoadingBinding, LoadingItem>() {

    override val viewBindingInflater: (
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ) -> ItemLoadingBinding = ItemLoadingBinding::inflate

    override fun areItemsTheSame(old: LoadingItem, new: LoadingItem): Boolean = true

    override fun areContentsTheSame(old: LoadingItem, new: LoadingItem): Boolean = true

    override fun isForItem(item: BaseItem): Boolean {
        return item is LoadingItem
    }
}