/*
 * Copyright (©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package com.d4rk.android.libs.apptoolkit.core.ui.base

import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.ActionEvent
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.d4rk.android.libs.apptoolkit.core.ui.state.UiStateScreen
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
abstract class ScreenViewModel<T, E : UiEvent, A : ActionEvent>(
    initialState: UiStateScreen<T>
) : BaseViewModel<UiStateScreen<T>, E, A>(initialState) {
    /** Mutable state backing the screen. */
    protected val screenState: MutableStateFlow<UiStateScreen<T>>
        get() = uiStateFlow

    /** Convenience accessor for the current data stored in the state. */
    protected val screenData: T?
        get() = currentState.data
}
