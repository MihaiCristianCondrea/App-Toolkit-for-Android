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