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

package com.d4rk.android.libs.apptoolkit.core.ui.state

import androidx.compose.runtime.Immutable
import com.d4rk.android.libs.apptoolkit.core.ui.base.handling.UiState
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenDataStatus
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.ScreenMessageType
import com.d4rk.android.libs.apptoolkit.core.utils.platform.UiTextHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Represents the comprehensive state of a UI screen.
 *
 * This data class encapsulates all the necessary information to render a screen at any given moment,
 * including the core data, loading/error status, and user-facing messages like snackbars.
 * It implements the [UiState] marker interface, integrating it into the app's state management pattern.
 *
 * @param T The type of the primary data held by the screen state.
 * @property screenState The current state of the screen (e.g., loading, success, error, no data). Defaults to [ScreenState.IsLoading].
 * @property errors A list of [UiSnackbar] objects representing errors that may need to be displayed to the user. Defaults to an empty list.
 * @property snackbar A single [UiSnackbar] to be shown immediately. This is nullable; a non-null value triggers the display. Defaults to null.
 * @property data The actual data of type [T] to be displayed on the screen. This is nullable and will typically be populated on a successful data fetch. Defaults to null.
 */
@Immutable
data class UiStateScreen<T>(
    val screenState: ScreenState = ScreenState.IsLoading(),
    val errors: List<UiSnackbar> = emptyList(),
    val snackbar: UiSnackbar? = null,
    val data: T? = null

) : UiState

/**
 * Represents a message to be displayed in a snackbar.
 * It encapsulates the content, type, and state of the message.
 *
 * @property type The type of the message, typically used for styling (e.g., error, success, info).
 *              Defaults to [ScreenMessageType.NONE].
 * @property message The actual content of the message, wrapped in a [UiTextHelper]
 *                   to support both static and dynamic strings.
 * @property isError A boolean flag indicating if this snackbar message represents an error.
 *                   This can be used for specific UI handling or logging. Defaults to `true`.
 * @property timeStamp A timestamp indicating when the snackbar was created. Can be used
 *                     to prevent showing the same message multiple times in quick succession.
 */
@Immutable
data class UiSnackbar(
    val type: String = ScreenMessageType.NONE,
    val message: UiTextHelper = UiTextHelper.DynamicString(content = ""),
    val isError: Boolean = true,
    val timeStamp: Long = 0,
)


/**
 * Updates the state of a `MutableStateFlow<UiStateScreen<T>>` with a new `ScreenState`
 * and a transformed data object.
 *
 * This is an inline extension function that atomically updates the `UiStateScreen` value.
 * It takes a new `ScreenState` to set and a `transform` lambda to modify the existing `data`.
 * The transformation is only applied if the current `data` is not null.
 *
 * @param T The type of the data held by `UiStateScreen`.
 * @param newState The new [ScreenState] to be set (e.g., `ScreenState.Success()`, `ScreenState.IsLoading()`).
 * @param transform A lambda function that takes the current data of type `T` and returns the updated data of the same type.
 */
inline fun <T> MutableStateFlow<UiStateScreen<T>>.updateData(
    newState: ScreenState, crossinline transform: (T) -> T
) {
    update { current ->
        current.copy(screenState = newState, data = current.data?.let { transform(it) })
    }
}

/**
 * Updates the `data` field of the current [UiStateScreen] within a [MutableStateFlow].
 *
 * This is an inline extension function that provides a concise way to modify the `data`
 * part of the UI state without changing the `screenState` or other properties. It uses
 * the `update` function of [MutableStateFlow] to ensure atomic updates.
 *
 * The transformation is applied only if the current `data` is not null.
 *
 * @param T The type of the data held by the [UiStateScreen].
 * @param transform A lambda function with the current data (`T`) as its receiver,
 *                  which returns the modified data (`T`).
 *
 * @see updateData for updating both `screenState` and `data`.
 * @see successData for updating `data` and setting the `screenState` to [ScreenState.Success].
 *
 * @sample
 * ```kotlin
 * // Assuming a ViewModel with a state flow:
 * // val uiState = MutableStateFlow(UiStateScreen(data = UserProfile(name = "John")))
 *
 * fun updateUserName(newName: String) {
 *     uiState.copyData {
 *         // 'this' refers to the UserProfile object
 *         this.copy(name = newName)
 *     }
 *     // The new state will be: UiStateScreen(data = UserProfile(name = "New Name"))
 *     // The screenState remains unchanged.
 * }
 * ```
 */
inline fun <T> MutableStateFlow<UiStateScreen<T>>.copyData(crossinline transform: T.() -> T) {
    update { current ->
        current.copy(data = current.data?.transform())
    }
}

/**
 * Updates the `UiStateScreen` to a success state and transforms the existing data.
 *
 * This inline extension function simplifies updating a `MutableStateFlow<UiStateScreen<T>>`.
 * It sets the `screenState` to `ScreenState.Success()` and applies a transformation
 * to the `data` property within the current state.
 *
 * The transformation is provided as a lambda function (`transform`) which receives the
 * current data `T` as its receiver. This allows for concise updates using a `copy`-like syntax.
 * If the current data is `null`, it remains `null`.
 *
 * @param T The type of the data held by the `UiStateScreen`.
 * @param transform A lambda function with `T` as its receiver, which returns the transformed `T`.
 *
 * @see ScreenState.Success
 * @see UiStateScreen
 *
 * @sample
 * ```kotlin
 * // Assuming `uiState` is a MutableStateFlow<UiStateScreen<MyData>>
 * // and MyData is a data class: data class MyData(val name: String, val value: Int)
 *
 * // Initial state
 * // uiState.value = UiStateScreen(data = MyData("Initial", 0))
 *
 * // Update the name and set the state to Success
 * uiState.successData {
 *     copy(name = "Updated Name")
 * }
 *
 * // The new state will be:
 * // UiStateScreen(
 * //   screenState = ScreenState.Success(),
 * //   data = MyData("Updated Name", 0)
 * // )
 * ```
 */
inline fun <T> MutableStateFlow<UiStateScreen<T>>.successData(crossinline transform: T.() -> T) {
    update { current ->
        current.copy(screenState = ScreenState.Success(), data = current.data?.transform())
    }
}

/**
 * Updates the `screenState` of the current `UiStateScreen`.
 *
 * This is an extension function for `MutableStateFlow<UiStateScreen<T>>` that allows for
 * concisely changing the state of the screen (e.g., to loading, success, error, etc.)
 * without modifying other parts of the UI state like `data` or `errors`.
 *
 * @param newValues The new [ScreenState] to be set.
 */
fun <T> MutableStateFlow<UiStateScreen<T>>.updateState(newValues: ScreenState) {
    update { current: UiStateScreen<T> ->
        current.copy(screenState = newValues)
    }
}

/**
 * Updates the `UiStateScreen` with a new list of errors.
 *
 * This extension function allows for updating the `errors` property of the current `UiStateScreen`
 * within a `MutableStateFlow`. It creates a new copy of the state with the provided list of `UiSnackbar`
 * errors, leaving other properties unchanged. This is typically used to display multiple, non-blocking
 * error messages or validation failures to the user.
 *
 * @param T The type of the data held by the `UiStateScreen`.
 * @param errors The new list of `UiSnackbar` objects to set as the current errors.
 */
fun <T> MutableStateFlow<UiStateScreen<T>>.setErrors(errors: List<UiSnackbar>) {
    update { current: UiStateScreen<T> ->
        current.copy(errors = errors)
    }
}

/**
 * Updates the UI state to display a snackbar message.
 * This is an extension function for `MutableStateFlow<UiStateScreen<T>>`.
 *
 * @param snackbar The [UiSnackbar] object containing the message and other details to be displayed.
 */
fun <T> MutableStateFlow<UiStateScreen<T>>.showSnackbar(snackbar: UiSnackbar) {
    update { current: UiStateScreen<T> ->
        current.copy(snackbar = snackbar)
    }
}

/**
 * Dismisses the current snackbar by setting it to null.
 *
 * This extension function updates the `UiStateScreen` within a `MutableStateFlow`
 * to remove the active snackbar, effectively hiding it from the UI.
 *
 * @param T The type of the data held within the `UiStateScreen`.
 */
fun <T> MutableStateFlow<UiStateScreen<T>>.dismissSnackbar() {
    update { current: UiStateScreen<T> ->
        current.copy(snackbar = null)
    }
}

/**
 * Updates the screen state to a loading state.
 *
 * This extension function sets the `screenState` property of the `UiStateScreen` to `ScreenState.IsLoading`,
 * indicating that a data-loading operation is in progress. The rest of the state remains unchanged.
 *
 * @receiver `MutableStateFlow<UiStateScreen<T>>` The state flow to be updated.
 * @param T The type of the data held by the `UiStateScreen`.
 */
fun <T> MutableStateFlow<UiStateScreen<T>>.setLoading() {
    update { current ->
        current.copy(screenState = ScreenState.IsLoading())
    }
}

/**
 * Sets the screen to a success state and replaces [UiStateScreen.data].
 *
 * Use this function when you have a new, complete data object and want to:
 * - set [UiStateScreen.screenState] to [ScreenState.Success]
 * - replace [UiStateScreen.data] with [data]
 *
 * This function does not show a snackbar.
 *
 * @param data The new data to set on the screen state.
 */
fun <T> MutableStateFlow<UiStateScreen<T>>.setSuccess(data: T) {
    update { current ->
        current.copy(
            screenState = ScreenState.Success(),
            data = data,
        )
    }
}

/**
 * Sets the screen to a "no data" state and replaces [UiStateScreen.data].
 */
fun <T> MutableStateFlow<UiStateScreen<T>>.setNoData(
    data: T,
    clearSnackbar: Boolean = true,
) {
    update { current ->
        current.copy(
            screenState = ScreenState.NoData(),
            data = data,
            snackbar = if (clearSnackbar) null else current.snackbar,
        )
    }
}

/**
 * Sets the screen to an error state and shows a snackbar.
 *
 * Use this function when a screen-level operation fails and you want to:
 * - mark the screen state as [ScreenState.Error]
 * - surface an error message through [UiSnackbar]
 *
 * This function does not modify [UiStateScreen.data]. It updates only:
 * - [UiStateScreen.screenState]
 * - [UiStateScreen.snackbar]
 *
 * @param message The message to show in the snackbar.
 * @param type The snackbar type. Defaults to [ScreenMessageType.SNACKBAR].
 * @param timeStamp A unique timestamp used to avoid duplicate snackbar rendering.
 *                  Defaults to [System.nanoTime].
 */
fun <T> MutableStateFlow<UiStateScreen<T>>.setError(
    message: UiTextHelper,
    type: String = ScreenMessageType.SNACKBAR,
    timeStamp: Long = System.nanoTime(),
) {
    update { current ->
        current.copy(
            screenState = ScreenState.Error(),
            snackbar = UiSnackbar(
                type = type,
                message = message,
                isError = true,
                timeStamp = timeStamp,
            ),
        )
    }
}

/**
 * Represents the distinct states of a UI screen, particularly concerning data loading and display.
 *
 * This sealed class is used within [UiStateScreen] to define the current visual state of the screen.
 * Each state can optionally hold a `data` string, which defaults to a constant from [ScreenDataStatus],
 * allowing for potential differentiation or custom handling in the UI layer.
 *
 * @see UiStateScreen.screenState
 */
sealed class ScreenState {
    data class NoData(val data: String = ScreenDataStatus.NO_DATA) : ScreenState()
    data class IsLoading(val data: String = ScreenDataStatus.LOADING) : ScreenState()
    data class Success(val data: String = ScreenDataStatus.HAS_DATA) : ScreenState()
    data class Error(val data: String = ScreenDataStatus.ERROR) : ScreenState()
}