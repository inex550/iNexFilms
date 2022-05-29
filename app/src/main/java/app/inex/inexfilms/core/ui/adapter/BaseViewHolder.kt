package app.inex.inexfilms.core.ui.adapter

import android.content.Context
import android.content.res.Resources
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<VB: ViewBinding>(
    val binding: VB
): RecyclerView.ViewHolder(binding.root) {

    val context: Context get() = binding.root.context

    val resources: Resources get() = context.resources
}