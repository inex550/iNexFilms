package app.inex.inexfilms.core.ui.ext

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import app.inex.inexfilms.core.ui.base.BaseViewState
import kotlinx.coroutines.flow.*

fun <VS: BaseViewState, T> StateFlow<VS>.renderIn(
    lifecycle: Lifecycle,
    renderProperty: (VS) -> T,
    render: (T) -> Unit
) {
    lifecycle.coroutineScope.launchWhenStarted {
        this@renderIn.flowWithLifecycle(
            lifecycle = lifecycle,
            minActiveState = Lifecycle.State.STARTED
        )
            .map(renderProperty)
            .distinctUntilChanged()
            .collect(render)
    }
}