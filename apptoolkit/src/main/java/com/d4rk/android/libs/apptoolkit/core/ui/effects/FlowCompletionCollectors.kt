package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.flow.onCompletion

@Composable
internal fun <T> Flow<T>.collectWithLifecycleOnCompletion(
    initialValue: T, // FIXME: Parameter 'initialValue' has runtime-determined stability
    onCompletion: (Throwable?) -> Unit = {},
) : State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val latestInitialValue by rememberUpdatedState(newValue = initialValue)
    val completionHandler by rememberUpdatedState(newValue = onCompletion)
    val state = remember { mutableStateOf(initialValue) }

    LaunchedEffect(this, lifecycleOwner) {
        this@collectWithLifecycleOnCompletion
            .flowWithLifecycle(lifecycleOwner.lifecycle , Lifecycle.State.STARTED)
            .onCompletion { cause : Throwable? ->
                if (cause != null && cause !is CancellationException) {
                    state.value = latestInitialValue
                }
                completionHandler(cause)
            }
            .collect { value : T ->
                state.value = value
            }
    }

    return state
}
