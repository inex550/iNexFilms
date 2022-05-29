package app.inex.inexfilms.presentation.filmdetails.vm

import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.ui.base.BaseViewAction

sealed class FilmDetailsViewAction: BaseViewAction {

    data class DialogError(val error: ErrorModel): FilmDetailsViewAction()
}
