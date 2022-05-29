package app.inex.inexfilms.presentation.splash.vm

import app.inex.inexfilms.core.ui.base.BaseViewState

data class SplashViewState(
    val isLoading: Boolean
): BaseViewState {

    companion object {
        fun provideInitialState(): SplashViewState {
            return SplashViewState(
                isLoading = true
            )
        }
    }
}