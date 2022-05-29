package app.inex.inexfilms.core.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import app.inex.inexfilms.core.ui.adapter.BaseItem
import app.inex.inexfilms.core.ui.adapter.DelegateAdapter

class AdaptersDiffCallback(
    private val oldItems: List<BaseItem>,
    private val newItems: List<BaseItem>,
    private val adapters: List<DelegateAdapter>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        adapters.find { it.isForItem(oldItem) && it.isForItem(newItem) }?.let{
            return it.checkItemsTheSame(oldItem, newItem)
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        adapters.find { it.isForItem(oldItem) && it.isForItem(newItem) }?.let {
            return it.checkContentsTheSame(oldItem, newItem)
        }
        return false
    }
}