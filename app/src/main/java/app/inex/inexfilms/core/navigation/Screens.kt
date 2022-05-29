package app.inex.inexfilms.core.navigation

import app.inex.inexfilms.presentation.filmdetails.ui.FilmDetailsFragment
import app.inex.inexfilms.presentation.filmslist.ui.FilmsListFragment
import app.inex.inexfilms.presentation.flow.FlowFragment
import app.inex.inexfilms.presentation.flow.TabFragment
import app.inex.inexfilms.presentation.sectiondetails.ui.SectionDetailsFragment
import app.inex.inexfilms.presentation.settings.ui.SettingsFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {

    fun flowScreen(): FragmentScreen = FragmentScreen {
        FlowFragment()
    }

    fun tabFragment(tag: String): FragmentScreen = FragmentScreen {
        TabFragment.newInstance(tag = tag)
    }

    fun filmsListScreen(): FragmentScreen = FragmentScreen {
        FilmsListFragment()
    }

    fun filmDetailsScreen(filmPage: String): FragmentScreen = FragmentScreen {
        FilmDetailsFragment.newInstance(filmPage = filmPage)
    }

    fun sectionDetailsScreen(sectionUri: String, sectionName: String): FragmentScreen = FragmentScreen {
        SectionDetailsFragment.newInstance(sectionUri = sectionUri, sectionName = sectionName)
    }

    fun settingsScreen(): FragmentScreen = FragmentScreen {
        SettingsFragment()
    }
}