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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.NavigationBackHandler
import androidx.navigationevent.compose.rememberNavigationEventState

/**
 * Handles back while [enabled], ahead of the displays composed before it.
 *
 * The shell's overlays, the drawer, the modal rail, the expanded player and a search query, use
 * this rather than the activity's `BackHandler`, so they share the navigation event dispatcher
 * with the displays and take priority by being registered after them.
 *
 * Registration waits for the next frame. A display inside a `Scaffold` is composed during layout,
 * after everything composed beside it, so a handler composed in the same pass as the display would
 * otherwise register first and lose to it.
 */
@Composable
fun ShellBackHandler(enabled: Boolean = true, onBack: () -> Unit) {
    val currentOnBack by rememberUpdatedState(onBack)
    var registered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        withFrameNanos { }
        registered = true
    }
    if (!registered) return
    NavigationBackHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = enabled,
        onBackCancelled = {},
        onBackCompleted = { currentOnBack() },
    )
}
