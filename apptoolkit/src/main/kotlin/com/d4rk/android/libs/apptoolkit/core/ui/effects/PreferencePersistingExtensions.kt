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

package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

/**
 * Persists changes emitted by a [MutableState] using the provided [onPersist] callback.
 *
 * The helper observes state changes, skips the initial value, and invokes [onError] when a
 * persistence failure occurs (defaulting to restoring the last known value).
 */
fun <T> MutableState<T>.persistChanges(
    scope: CoroutineScope,
    currentValue: () -> T,
    onPersist: suspend (T) -> Unit,
    onError: (Throwable, T) -> Unit = { _, latest -> value = latest },
) {
    scope.launch {
        snapshotFlow { value }
            .distinctUntilChanged()
            .drop(1)
            .onCompletion { cause: Throwable? ->
                if (cause != null && cause !is CancellationException) {
                    onError(cause, currentValue())
                }
            }
            .collectLatest { newValue: T ->
                onPersist(newValue)
            }
    }
}
