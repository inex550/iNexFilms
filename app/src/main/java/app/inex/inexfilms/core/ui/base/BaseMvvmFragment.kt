package app.inex.inexfilms.core.ui.base

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class BaseMvvmFragment <VS: BaseViewState, VA: BaseViewAction, VM: BaseViewModel<VS, VA>>(
    @LayoutRes layoutResId: Int
): BaseFragment(layoutResId) {

    open fun setupRender() = Unit
    open fun proceedAction(action: VA) = Unit

    abstract val viewModel: VM

    protected val uiState: VS
        get() = viewModel.uiState.value

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRender()

        lifecycleScope.launch {
            viewModel.action.flowWithLifecycle(
                lifecycle = viewLifecycle,
                minActiveState = Lifecycle.State.STARTED
            ).collect(::proceedAction)
        }

        super.onViewCreated(view, savedInstanceState)
    }
}