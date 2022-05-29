package app.inex.inexfilms.presentation.flow

import android.os.Bundle
import android.view.KeyEvent
import app.inex.inexfilms.R
import app.inex.inexfilms.core.common.ext.orFalse
import app.inex.inexfilms.core.navigation.RouterProvider
import app.inex.inexfilms.core.navigation.Screens
import app.inex.inexfilms.core.navigation.sub.CiceroneHolder
import app.inex.inexfilms.core.ui.base.BaseFragment
import app.inex.inexfilms.core.ui.ext.withArgs
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import com.github.terrakok.cicerone.androidx.FragmentScreen

class TabFragment: RouterProvider, BaseFragment (
    layoutResId = R.layout.fragment_tab
) {
    private val screenTag: String by lazy {
        requireArguments().getString(ARG_TAG).orEmpty()
    }

    private val cicerone: Cicerone<Router> by lazy {
        CiceroneHolder.getCicerone(tag = screenTag)
    }

    private val router: Router
        get() = cicerone.router

    private val navigatorHolder: NavigatorHolder
        get() = cicerone.getNavigatorHolder()

    private val navigator: AppNavigator by lazy {
        AppNavigator(requireActivity(), R.id.tab_container, childFragmentManager)
    }

    override fun provideRouter(): Router {
        return router
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            router.newRootScreen(screenByTag())
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    private fun screenByTag(): FragmentScreen = when(screenTag) {
        FlowTags.TAG_FILMS_LIST -> Screens.filmsListScreen()
        FlowTags.TAG_SETTINGS -> Screens.settingsScreen()
        else -> throw NoSuchElementException("$screenTag not found")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?) {
        visibleFragment()?.onKeyDown(keyCode, event)
    }

    private fun visibleFragment(): BaseFragment? =
        childFragmentManager.fragments.lastOrNull { it.isVisible } as? BaseFragment

    override fun onBackPressed(): Boolean {
        return visibleFragment()?.onBackPressed().orFalse()
    }

    companion object {
        fun newInstance(tag: String): TabFragment = TabFragment().withArgs {
            putString(ARG_TAG, tag)
        }

        private const val ARG_TAG = "ATG_TAG"
    }
}