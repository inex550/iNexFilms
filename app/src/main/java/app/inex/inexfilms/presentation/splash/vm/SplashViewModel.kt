package app.inex.inexfilms.presentation.splash.vm

import app.inex.inexfilms.core.ui.base.BaseViewModel
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

class SplashViewModel @Inject constructor(
    private val router: Router
): BaseViewModel<SplashViewState, SplashViewAction>(
    initialState = SplashViewState.provideInitialState()
) {


}