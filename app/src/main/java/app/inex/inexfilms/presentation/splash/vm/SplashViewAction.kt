package app.inex.inexfilms.presentation.splash.vm

import app.inex.inexfilms.core.ui.base.BaseViewAction

sealed class SplashViewAction: BaseViewAction {

    data class Error(val message: String): SplashViewAction()
}