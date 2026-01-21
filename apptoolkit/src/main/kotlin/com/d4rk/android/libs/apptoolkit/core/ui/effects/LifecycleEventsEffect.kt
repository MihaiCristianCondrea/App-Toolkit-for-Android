package com.d4rk.android.libs.apptoolkit.core.ui.effects

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * A side effect that listens for specific [Lifecycle.Event] transitions in the current
 * [LocalLifecycleOwner].
 *
 * This effect automatically handles the registration and unregistration of a
 * [LifecycleEventObserver] when the Composable enters or leaves the composition.
 *
 * @param events The lifecycle events to observe.
 * @param onEvent A callback invoked when any of the specified [events] occur.
 */
@Composable
fun LifecycleEventsEffect(
    vararg events: Lifecycle.Event,
    onEvent: (Lifecycle.Event) -> Unit,
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val latestOnEvent by rememberUpdatedState(newValue = onEvent)

    DisposableEffect(lifecycleOwner, *events) {
        val observer = LifecycleEventObserver { _, event ->
            if (events.contains(event)) {
                latestOnEvent(event)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}