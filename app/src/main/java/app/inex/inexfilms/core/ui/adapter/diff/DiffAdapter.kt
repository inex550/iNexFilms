package app.inex.inexfilms.core.ui.adapter.diff

import app.inex.inexfilms.core.ui.adapter.BaseItem

interface DiffAdapter<T: BaseItem> {

    fun areItemsTheSame(oldItem: T, newItem: T): Boolean

    fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    fun getPayload(oldItem: T): Any?
}