package app.inex.inexfilms.presentation.sectiondetails.vm

import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.ui.base.BaseViewAction

sealed interface SectionDetailsViewAction: BaseViewAction {

    data class DialogError(val error: ErrorModel): SectionDetailsViewAction
}