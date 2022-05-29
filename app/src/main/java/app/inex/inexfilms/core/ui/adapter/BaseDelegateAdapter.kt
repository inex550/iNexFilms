package app.inex.inexfilms.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseDelegateAdapter<VB: ViewBinding, T: BaseItem>: DelegateAdapter {

    abstract val viewBindingInflater: (
        inflater: LayoutInflater,
        parent: ViewGroup,
        attachToParent: Boolean
    ) -> VB

    open fun BaseViewHolder<VB>.onCreate() = Unit

    open fun BaseViewHolder<VB>.onBind(item: T) = Unit

    open fun areItemsTheSame(old: T, new: T): Boolean = false

    open fun areContentsTheSame(old: T, new: T): Boolean = false

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean): RecyclerView.ViewHolder {
        return BaseViewHolder(viewBindingInflater(inflater, parent, attachToParent)).apply {
            onCreate()
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseItem) {
        (holder as BaseViewHolder<VB>).onBind(item as T)
    }

    @Suppress("UNCHECKED_CAST")
    override fun checkItemsTheSame(old: BaseItem, new: BaseItem): Boolean {
        return areItemsTheSame(old as T, new as T)
    }

    @Suppress("UNCHECKED_CAST")
    override fun checkContentsTheSame(old: BaseItem, new: BaseItem): Boolean {
        return areContentsTheSame(old as T, new as T)
    }
}