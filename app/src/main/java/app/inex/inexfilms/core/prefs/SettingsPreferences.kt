package app.inex.inexfilms.core.prefs

import android.content.SharedPreferences
import app.inex.inexfilms.core.prefs.utils.StringPreference
import javax.inject.Inject

class SettingsPreferences @Inject constructor(
    prefs: SharedPreferences
) {

    var lordFilmBaseUrl by StringPreference(prefs, PREF_LORD_FILM_BASE_URL, DEFAULT_LORD_FILMS_BASE_URL)

    var lordFilmUrl by StringPreference(prefs, PREF_LORD_FILM_URL, DEFAULT_LORD_FILMS_BASE_URL)

    companion object {
        private const val PREF_LORD_FILM_BASE_URL = "PREF_LORD_FILM_BASE_URL"
        private const val PREF_LORD_FILM_URL = "PREF_LORD_FILMS_URL"

        private const val DEFAULT_LORD_FILMS_BASE_URL = "https://lordfilm.pet"
    }
}