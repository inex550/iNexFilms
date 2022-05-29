package app.inex.inexfilms.presentation.settings.vm

import app.inex.inexfilms.App
import app.inex.inexfilms.R
import app.inex.inexfilms.core.network.di.RefreshUriOkHttpClient
import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.network.ext.await
import app.inex.inexfilms.core.prefs.SettingsPreferences
import app.inex.inexfilms.core.ui.base.BaseViewModel
import com.github.terrakok.cicerone.Router
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    @RefreshUriOkHttpClient
    private val okHttpClient: OkHttpClient,
    private val prefs: SettingsPreferences,
    private val router: Router
): BaseViewModel<SettingsViewState, SettingsViewAction>(
    initialState = SettingsViewState.provideInitialState(
        baseUri = prefs.lordFilmBaseUrl,
        usefulUri = prefs.lordFilmUrl,
    )
) {

    override fun provideDefaultError(error: ErrorModel): SettingsViewAction {
        return SettingsViewAction.DialogError(error)
    }

    fun refreshUri() {
        launchIOCoroutine(
            finallyListener = { updateState { copy(refreshLoading = false) } }
        ) {
            updateState { copy(refreshLoading = true) }

            val request = Request.Builder()
                .url(prefs.lordFilmBaseUrl)
                .build()

            val resp = okHttpClient.newCall(request).await()
            var newUri = resp.headers[HEADER_LOCATION]

            if (newUri == null) {
                performAction(provideDefaultError(error = ErrorModel(
                    message = App.context.getString(R.string.error_link_not_received)
                )))
                return@launchIOCoroutine
            }

            newUri = newUri.removeSuffix("/")

            prefs.lordFilmUrl = newUri

            updateState { copy(usefulUri = newUri) }
        }
    }

    fun saveUriSettings(
        baseUri: String,
        usefulUri: String,
    ) {
        prefs.lordFilmBaseUrl = baseUri
        prefs.lordFilmUrl = usefulUri

        performAction(SettingsViewAction.ToastShort(
            App.context.getString(R.string.label_settings_saved)
        ))
    }

    fun onBackPressed() {
        router.exit()
    }

    companion object {
        const val HEADER_LOCATION = "Location"
    }
}