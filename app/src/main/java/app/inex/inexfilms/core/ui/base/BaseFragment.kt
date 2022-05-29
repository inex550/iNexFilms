package app.inex.inexfilms.core.ui.base

import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import app.inex.inexfilms.core.navigation.RouterProvider
import com.github.terrakok.cicerone.Router


abstract class BaseFragment(
    @LayoutRes private val layoutResId: Int
): Fragment(layoutResId) {

    open fun setupInjection() = Unit

    open fun setupUi() = Unit

    open fun onKeyDown(keyCode: Int, event: KeyEvent?) = Unit

    open fun onBackPressed(): Boolean = false

    val viewLifecycle: Lifecycle
        get() = viewLifecycleOwner.lifecycle

    val localRouter: Router
        get() = (parentFragment as? RouterProvider)?.provideRouter() ?: (requireActivity() as RouterProvider).provideRouter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupInjection()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUi()
    }
}