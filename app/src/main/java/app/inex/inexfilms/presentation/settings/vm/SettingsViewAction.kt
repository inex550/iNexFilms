package app.inex.inexfilms.presentation.settings.vm

import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.ui.base.BaseViewAction

sealed interface SettingsViewAction: BaseViewAction {

    data class DialogError(val errorModel: ErrorModel): SettingsViewAction

    data class ToastShort(val message: String): SettingsViewAction
}
