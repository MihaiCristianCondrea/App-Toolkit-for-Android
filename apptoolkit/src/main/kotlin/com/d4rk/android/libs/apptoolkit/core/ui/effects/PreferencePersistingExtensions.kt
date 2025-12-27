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
