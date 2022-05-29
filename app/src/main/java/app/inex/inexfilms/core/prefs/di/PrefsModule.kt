package app.inex.inexfilms.core.prefs.di

import android.content.Context
import android.content.SharedPreferences
import app.inex.inexfilms.App
import app.inex.inexfilms.R
import app.inex.inexfilms.core.prefs.SettingsPreferences
import dagger.Module
import dagger.Provides

@Module
class PrefsModule {

    @Provides
    fun provideSharedPreferences(): SharedPreferences {
        return App.context.getSharedPreferences(
            App.context.getString(R.string.settings_prefs_filename),
            Context.MODE_PRIVATE
        )
    }
}