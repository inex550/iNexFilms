package app.inex.inexfilms.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.inex.inexfilms.BuildConfig
import app.inex.inexfilms.core.network.error.ErrorModel
import app.inex.inexfilms.core.network.error.ErrorParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

abstract class BaseViewModel<VS: BaseViewState, VA: BaseViewAction>(
    initialState: VS,
): ViewModel() {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<VS> = _uiState

    private val _action = MutableSharedFlow<VA>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val action: SharedFlow<VA> = _action

    fun updateState(newState: VS.() -> VS) {
        _uiState.value = _uiState.value.newState()
    }

    fun performAction(action: VA) {
        _action.tryEmit(action)
    }

    open fun provideDefaultError(error: ErrorModel): VA? = null

    protected fun launchCoroutine(
        errorListener: ErrorListener = ErrorListener { true },
        finallyListener: FinallyListener? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            try {
                block()
            } catch (t: Throwable) {
                Timber.e(t)

                val error = ErrorParser.parseThrowable(t)
                val listenerResult = errorListener.proceedError(error)

                if (listenerResult) {
                    provideDefaultError(error)?.let {
                        performAction(it)
                    } ?: throw NotImplementedError("\"provideDefaultError\" at ${this@BaseViewModel::class} not implemented")
                }
            } finally {
                finallyListener?.finally()
            }
        }
    }

    protected fun launchIOCoroutine(
        errorListener: ErrorListener = ErrorListener { true },
        finallyListener: FinallyListener? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        launchCoroutine(
            errorListener = errorListener,
            finallyListener = finallyListener
        ) {
            withContext(Dispatchers.IO, block)
        }
    }

    protected fun launchDefaultCoroutine(
        errorListener: ErrorListener = ErrorListener { true },
        finallyListener: FinallyListener? = null,
        block: suspend CoroutineScope.() -> Unit
    ) {
        launchCoroutine(
            errorListener = errorListener,
            finallyListener = finallyListener
        ) {
            withContext(Dispatchers.Default, block)
        }
    }

    fun interface ErrorListener {
        fun proceedError(error: ErrorModel): Boolean
    }

    fun interface FinallyListener {
        fun finally()
    }
}