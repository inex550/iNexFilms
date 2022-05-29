package app.inex.inexfilms.core.ui.adapter.diff

import androidx.recyclerview.widget.DiffUtil
import app.inex.inexfilms.core.ui.adapter.BaseItem

class AdapterDiffCallback<T: BaseItem>(
    private val diffAdapter: DiffAdapter<T>,
    private val oldItems: List<T>,
    private val newItems: List<T>,
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldItems.size

    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return diffAdapter.areItemsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return diffAdapter.areContentsTheSame(oldItems[oldItemPosition], newItems[newItemPosition])
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}