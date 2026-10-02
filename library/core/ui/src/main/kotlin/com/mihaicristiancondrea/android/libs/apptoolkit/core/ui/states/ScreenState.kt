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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states

import androidx.compose.runtime.Immutable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenDataStatus
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.ScreenMessageType
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.platform.UiTextHelper
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

/**
 * Legacy screen-wide state containing content, loading status, and snackbar requests. [errors]
 * holds additional messages; [snackbar] requests immediate display.
 */
@Immutable
data class UiStateScreen<T>(
    val screenState: ScreenState = ScreenState.IsLoading(),
    val errors: List<UiSnackbar> = emptyList(),
    val snackbar: UiSnackbar? = null,
    val data: T? = null

) : UiState

/**
 * Snackbar request with unresolved text and optional action.
 *
 * @property timeStamp Identifies repeated messages so the handler can distinguish them.
 * @property actionLabel Optional action label; `DefaultSnackbarHandler` emits the corresponding
 * action event.
 */
@Immutable
data class UiSnackbar(
    val type: String = ScreenMessageType.NONE,
    val message: UiTextHelper = UiTextHelper.DynamicString(content = ""),
    val isError: Boolean = true,
    val timeStamp: Long = 0,
    val actionLabel: UiTextHelper? = null,
)


/**
 * Atomically changes the screen status and transforms existing data. The transform is skipped
 * when data is null.
 */
inline fun <T> MutableStateFlow<UiStateScreen<T>>.updateData(
    newState: ScreenState, crossinline transform: (T) -> T
) {
    update { current ->
        current.copy(screenState = newState, data = current.data?.let { transform(it) })
    }
}

/**
 * Atomically transforms non-null data without changing the screen status or messages.
 */
inline fun <T> MutableStateFlow<UiStateScreen<T>>.copyData(crossinline transform: T.() -> T) {
    update { current ->
        current.copy(data = current.data?.transform())
    }
}

/**
 * Sets success status and atomically transforms existing data; null data remains null.
 */
inline fun <T> MutableStateFlow<UiStateScreen<T>>.successData(crossinline transform: T.() -> T) {
    update { current ->
        current.copy(screenState = ScreenState.Success(), data = current.data?.transform())
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.updateState(newValues: ScreenState) {
    update { current: UiStateScreen<T> ->
        current.copy(screenState = newValues)
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.setErrors(errors: List<UiSnackbar>) {
    update { current: UiStateScreen<T> ->
        current.copy(errors = errors)
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.showSnackbar(snackbar: UiSnackbar) {
    update { current: UiStateScreen<T> ->
        current.copy(snackbar = snackbar)
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.dismissSnackbar() {
    update { current: UiStateScreen<T> ->
        current.copy(snackbar = null)
    }
}

fun <T> MutableStateFlow<UiStateScreen<T>>.setLoading() {
    update { current ->
        current.copy(screenState = ScreenState.IsLoading())
    }
}

/**
 * Sets success status and replaces data, preserving existing messages.
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
 * Sets no-data status and replaces data. Clears the current snackbar unless [clearSnackbar] is
 * `false`.
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
 * Sets error status and an error snackbar while retaining current data.
 *
 * @param timeStamp Distinguishes this request from repeated messages.
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
 * Legacy screen-wide loading status. Each case carries the label used for status reporting.
 */
sealed class ScreenState {
    data class NoData(val data: String = ScreenDataStatus.NO_DATA) : ScreenState()
    data class IsLoading(val data: String = ScreenDataStatus.LOADING) : ScreenState()
    data class Success(val data: String = ScreenDataStatus.HAS_DATA) : ScreenState()
    data class Error(val data: String = ScreenDataStatus.ERROR) : ScreenState()
}