package app.inex.inexfilms.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import app.inex.inexfilms.core.ui.adapter.diff.DiffAdapter
import app.inex.inexfilms.core.ui.adapter.diff.AdapterDiffCallback

abstract class BaseAdapter<T: BaseItem, VB: ViewBinding>: RecyclerView.Adapter<BaseViewHolder<VB>>(),
    DiffAdapter<T> {

    private var _items: MutableList<T> = mutableListOf()

    abstract val viewBindingInflater: (
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ) -> VB

    fun getItems(): List<T> = _items

    fun setItems(newItems: List<T>) {
        val diffCallback = AdapterDiffCallback(this, oldItems = _items, newItems = newItems)
        DiffUtil.calculateDiff(diffCallback)
            .dispatchUpdatesTo(this)

        _items = ArrayList(newItems)
    }

    fun addItems(newItems: List<T>) {
        val startPosition = _items.size

        _items.addAll(newItems)
        notifyItemRangeInserted(startPosition, newItems.size)
    }

    fun removeItem(item: T) {
        val indexOfRemove = _items.indexOf(item)

        _items.removeAt(indexOfRemove)
        notifyItemRemoved(indexOfRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> =
        BaseViewHolder(viewBindingInflater(LayoutInflater.from(parent.context), parent, false)).apply {
            onCreate()
        }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        holder.onBind(item = _items[position])
    }

    override fun getItemCount(): Int = _items.size

    open fun BaseViewHolder<VB>.onCreate() = Unit

    open fun BaseViewHolder<VB>.onBind(item: T) = Unit

    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = false

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = false

    override fun getPayload(oldItem: T): Any? = null
}