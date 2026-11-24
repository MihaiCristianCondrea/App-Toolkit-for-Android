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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch

/**
 * Collects values from this [Flow] in a lifecycle-aware manner and provides a completion callback.
 *
 * The flow is collected only when the lifecycle is at least in the [Lifecycle.State.STARTED] state.
 * If the flow fails with an exception (other than [CancellationException]), the failure does not
 * escape the effect: the state is reset to the value provided by [initialValueProvider] and
 * [onCompletion] receives the state and failure so callers can adjust the fallback value without
 * it being overwritten. When the collecting effect is cancelled, [onCompletion] is not called.
 *
 * @param T The type of the values contained in the flow.
 * @param initialValueProvider A lambda providing the initial value for the state and the reset value upon failure.
 * @param onCompletion A callback invoked when the flow completes, providing the [MutableState] and
 * the [Throwable] cause if an error occurred.
 * @return A [State] object containing the latest emitted value or the initial value if reset.
 */
@Composable
internal fun <T> Flow<T>.collectWithLifecycleOnCompletion(
    initialValueProvider: () -> T,
    onCompletion: (MutableState<T>, Throwable?) -> Unit = { _, _ -> },
): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val latestInitialValueProvider by rememberUpdatedState(newValue = initialValueProvider)
    val completionHandler by rememberUpdatedState(newValue = onCompletion)
    val state = remember { mutableStateOf(value = initialValueProvider()) }

    LaunchedEffect(this, lifecycleOwner) {
        var failure: Throwable? = null
        this@collectWithLifecycleOnCompletion
            .flowWithLifecycle(lifecycle = lifecycleOwner.lifecycle, minActiveState = Lifecycle.State.STARTED)
            .catch { cause: Throwable ->
                if (cause is CancellationException) throw cause
                failure = cause
                state.value = latestInitialValueProvider()
            }
            .collect { value: T ->
                state.value = value
            }
        completionHandler(state, failure)
    }

    return state
}
