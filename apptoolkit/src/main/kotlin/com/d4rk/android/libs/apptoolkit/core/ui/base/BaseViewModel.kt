package com.d4rk.android.libs.apptoolkit.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiState
import com.d4rk.android.libs.apptoolkit.core.ui.state.ScreenState
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Base class for ViewModels used throughout the toolkit.
 *
 * The class exposes state updates via [uiState] and one-off events via
 * [actionEvent]. Concrete implementations handle incoming events through
 * [onEvent] and can emit new actions with [sendAction].
 *
 * @param S type representing the UI state
 * @param E type of events coming from the UI layer
 * @param A type of one-off actions to be processed by the UI
 * @param initialState initial value of the state
 */
abstract class BaseViewModel<S : UiState, E : UiEvent, A : ActionEvent>(initialState: S) :
    ViewModel() {

    private val stateMutex = Mutex()

    protected val uiStateFlow: MutableStateFlow<S> = MutableStateFlow(value = initialState)

    /** Current state exposed to the UI as a [StateFlow]. */
    val uiState: StateFlow<S> = uiStateFlow.asStateFlow()

    private val _actionEvent = MutableSharedFlow<A>(extraBufferCapacity = 1)

    /** One-off actions that the UI should react to. */
    val actionEvent: SharedFlow<A> = _actionEvent.asSharedFlow()

    protected val currentState: S
        get() = uiState.value

    /**
     * General-purpose job for screens that only need one cancellable operation at a time.
     *
     * Prefer specialized job properties when multiple concurrent jobs are required.
     */
    protected var generalJob: Job? = null

    /** Handles a new UI [event]. */
    abstract fun onEvent(event: E)

    /** Emits an [action] for the UI to handle. */
    protected fun sendAction(action: A) {
        viewModelScope.launch {
            _actionEvent.emit(action)
        }
    }

    /**
     * Updates the current UI state in a thread-safe manner using a [Mutex].
     *
     * This ensures that concurrent state updates do not result in race conditions,
     * guaranteeing atomicity when modifying the [uiStateFlow].
     *
     * @param update A lambda function containing the logic to update the state.
     */
    protected suspend fun updateStateThreadSafe(update: () -> Unit) {
        stateMutex.withLock {
            update()
        }
    }

    /**
     * Updates [UiStateScreen.data] only when [UiStateScreen.screenState] is [ScreenState.Success].
     *
     * Mirrors the original "updateSuccessState" pattern (mutex + success-only update),
     * adapted for [UiStateScreen] since [ScreenState] is not generic in this codebase.
     */
    protected suspend fun <T> updateSuccessState(
        screenData: MutableStateFlow<UiStateScreen<T>>,
        updateData: (T) -> T,
    ) {
        stateMutex.withLock {
            getSuccessData(screenData)?.let { data ->
                screenData.value = screenData.value.copy(data = updateData(data))
            }
        }
    }

    /**
     * Returns the current non-null [UiStateScreen.data] only when the state is [ScreenState.Success].
     */
    protected fun <T> getSuccessData(screenData: MutableStateFlow<UiStateScreen<T>>): T? {
        val current = screenData.value
        if (current.screenState !is ScreenState.Success) return null
        return current.data
    }
}
