package app.inex.inexfilms.presentation.settings.vm

import app.inex.inexfilms.core.ui.base.BaseViewState

data class SettingsViewState(
    val refreshLoading: Boolean,
    val baseUri: String,
    val usefulUri: String,
): BaseViewState {

    companion object {
        fun provideInitialState(baseUri: String, usefulUri: String): SettingsViewState {
            return SettingsViewState(
                refreshLoading = false,
                baseUri = baseUri,
                usefulUri = usefulUri
            )
        }
    }
}