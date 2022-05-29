package app.inex.inexfilms.core.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter {

    fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup, attachToParent: Boolean): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: BaseItem)

    fun checkItemsTheSame(old: BaseItem, new: BaseItem): Boolean

    fun checkContentsTheSame(old: BaseItem, new: BaseItem): Boolean

    fun isForItem(item: BaseItem): Boolean
}