package app.inex.inexfilms.core.di

import app.inex.inexfilms.presentation.filmslist.ui.FilmsListFragment
import app.inex.inexfilms.AppActivity
import app.inex.inexfilms.core.navigation.di.NavigationModule
import app.inex.inexfilms.core.network.di.NetworkModule
import app.inex.inexfilms.core.prefs.SettingsPreferences
import app.inex.inexfilms.core.prefs.di.PrefsModule
import app.inex.inexfilms.presentation.filmdetails.ui.FilmDetailsFragment
import app.inex.inexfilms.presentation.filmslist.vm.FilmsListViewModel
import app.inex.inexfilms.presentation.sectiondetails.ui.SectionDetailsFragment
import app.inex.inexfilms.presentation.settings.ui.SettingsFragment
import app.inex.inexfilms.presentation.settings.vm.SettingsViewModel
import com.google.gson.Gson
import dagger.Component
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NavigationModule::class,
        NetworkModule::class,
        PrefsModule::class
    ]
)
interface AppComponent {

    fun inject(activity: AppActivity)

    fun inject(fragment: FilmsListFragment)
    fun inject(fragment: FilmDetailsFragment)
    fun inject(fragment: SectionDetailsFragment)
    fun inject(fragment: SettingsFragment)

    fun settingsViewModel(): SettingsViewModel

    fun settingsPreferences(): SettingsPreferences
    fun okHttpClient(): OkHttpClient
    fun gson(): Gson
}