package app.inex.inexfilms.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.inex.inexfilms.core.ui.adapter.diff.AdaptersDiffCallback

class ConcatAdapter(
    vararg adapters: DelegateAdapter
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val _items: MutableList<BaseItem> = mutableListOf()
    private val adapters: MutableList<DelegateAdapter> = mutableListOf()

    fun getItems(): List<BaseItem> = _items

    fun setItems(items: List<BaseItem>) {
        val diffCallback = AdaptersDiffCallback(_items, items, adapters)
        DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this)

        _items.clear()
        _items.addAll(items)
    }

    init {
        this.adapters.addAll(adapters)
    }

    override fun getItemViewType(position: Int): Int {
        val item = _items[position]
        val viewType = adapters.indexOfFirst { it.isForItem(item) }
        if (viewType < 0) throw NoSuchElementException("adapter for $item at $position not found")
        return viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapters[viewType].onCreateViewHolder(LayoutInflater.from(parent.context), parent, false)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapters[getItemViewType(position)].onBindViewHolder(holder, _items[position])
    }

    override fun getItemCount(): Int = _items.size
}