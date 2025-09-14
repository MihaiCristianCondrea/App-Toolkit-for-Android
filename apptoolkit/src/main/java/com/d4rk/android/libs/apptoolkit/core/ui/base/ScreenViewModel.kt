package com.d4rk.android.libs.apptoolkit.core.ui.base

import com.d4rk.android.libs.apptoolkit.core.domain.model.ui.UiStateScreen
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * Base ViewModel for screens that expose [UiStateScreen] as state.
 *
 * Provides convenient accessors for the current screen state and the
 * underlying data object [T].
 *
 * @param T type of data rendered on the screen
 * @param E events emitted by the UI
 * @param A one-off actions to be handled by the UI
 * @param initialState starting state of the screen
 */
abstract class ScreenViewModel<T , E : UiEvent , A : ActionEvent>(
    initialState : UiStateScreen<T>
) : BaseViewModel<UiStateScreen<T> , E , A>(initialState) {
    /** Mutable state backing the screen. */
    protected val screenState : MutableStateFlow<UiStateScreen<T>>
        get() = _uiState

    /** Convenience accessor for the current data stored in the state. */
    protected val screenData : T?
        get() = currentState.data
}
