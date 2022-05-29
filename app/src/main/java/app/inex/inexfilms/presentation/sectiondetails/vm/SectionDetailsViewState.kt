package app.inex.inexfilms.presentation.sectiondetails.vm

import app.inex.inexfilms.core.ui.adapter.BaseItem
import app.inex.inexfilms.core.ui.base.BaseViewState

data class SectionDetailsViewState(
    val isLoading: Boolean,
    val items: List<BaseItem>,
    val error: String?,
): BaseViewState {

    companion object {
        fun provideInitialState(): SectionDetailsViewState {
            return SectionDetailsViewState(
                isLoading = true,
                items = listOf(),
                error = null,
            )
        }
    }
}