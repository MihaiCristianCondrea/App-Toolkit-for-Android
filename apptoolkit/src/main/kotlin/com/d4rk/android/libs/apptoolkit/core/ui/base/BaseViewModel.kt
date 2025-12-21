package com.d4rk.android.libs.apptoolkit.core.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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

    protected val uiStateFlow: MutableStateFlow<S> = MutableStateFlow(value = initialState)

    /** Current state exposed to the UI as a [StateFlow]. */
    val uiState: StateFlow<S> = uiStateFlow.asStateFlow()

    private val _actionEvent = MutableSharedFlow<A>(extraBufferCapacity = 1)

    /** One-off actions that the UI should react to. */
    val actionEvent: SharedFlow<A> = _actionEvent.asSharedFlow()

    protected val currentState: S
        get() = uiState.value

    /** Handles a new UI [event]. */
    abstract fun onEvent(event: E)

    /** Emits an [action] for the UI to handle. */
    protected fun sendAction(action: A) {
        viewModelScope.launch {
            _actionEvent.emit(action)
        }
    }
}
