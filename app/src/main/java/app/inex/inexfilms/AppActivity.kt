package app.inex.inexfilms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import app.inex.inexfilms.core.common.ext.isTrue
import app.inex.inexfilms.core.navigation.RouterProvider
import app.inex.inexfilms.core.navigation.Screens
import app.inex.inexfilms.core.ui.base.BaseFragment
import app.inex.inexfilms.databinding.ActivityAppBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class AppActivity : AppCompatActivity(), RouterProvider {

    @Inject
    lateinit var router: Router

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    private lateinit var binding: ActivityAppBinding

    private val navigator = AppNavigator(this, R.id.container)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        App.component.inject(activity = this)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.flowScreen())
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        visibleFragment()?.onKeyDown(keyCode, event)
        return super.onKeyDown(keyCode, event)
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }

    override fun provideRouter(): Router {
        return router
    }

    private fun visibleFragment(): BaseFragment? =
        supportFragmentManager.fragments.firstOrNull { it.isVisible } as? BaseFragment

    override fun onBackPressed() {
        val backProceed = visibleFragment()?.onBackPressed().isTrue()
        if (!backProceed) super.onBackPressed()
    }
}