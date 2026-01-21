package com.d4rk.android.libs.apptoolkit.core.ui.effects

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

/**
 * Collects a [Flow] as a [State] specifically designed for DataStore operations within Compose.
 *
 * This function monitors the flow's lifecycle and provides specialized error handling.
 * If the flow completes due to an exception (excluding [CancellationException]), it logs the error,
 * resets the state to its [initial] value, and triggers the [onErrorReset] callback to allow
 * for custom recovery logic.
 *
 * @param T The type of data being collected.
 * @param initial A provider for the initial value and the fallback value in case of an error.
 * @param logTag The tag used for logging error messages.
 * @param onErrorReset A callback invoked when an error occurs, providing access to the [MutableState]
 * for manual intervention or UI synchronization.
 * @return A [State] object representing the current value of the DataStore flow.
 */
@Composable
fun <T> Flow<T>.collectDataStoreState(
    initial: () -> T,
    logTag: String,
    onErrorReset: (MutableState<T>) -> Unit = {},
): State<T> {
    val mutableState = remember { mutableStateOf(initial()) }
    val latestMutableState by rememberUpdatedState(mutableState)
    val latestInitial by rememberUpdatedState(initial)
    val latestOnErrorReset by rememberUpdatedState(onErrorReset)

    val collectedState = collectWithLifecycleOnCompletion(
        initialValueProvider = initial,
        onCompletion = { cause: Throwable? ->
            if (cause != null && cause !is CancellationException) {
                Log.w(logTag, "DataStore flow completed with an error.", cause)
                val initialValue = latestInitial()
                latestMutableState.value = initialValue
                latestOnErrorReset(latestMutableState)
            }
        },
    )

    LaunchedEffect(collectedState.value) {
        latestMutableState.value = collectedState.value
    }

    return mutableState
}
