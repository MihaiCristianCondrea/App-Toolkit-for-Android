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

package com.d4rk.android.libs.apptoolkit.core.ui.views.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

/**
 * Creates and remembers a [TopAppBarState] that starts in a collapsed position.
 *
 * This helper waits until Material 3 calculates [TopAppBarState.heightOffsetLimit],
 * then applies that limit as the current [TopAppBarState.heightOffset] exactly once.
 *
 * It is intended for usage such as:
 * `val state = rememberCollapsedTopAppBarState()` and
 * `val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(state)`.
 *
 * @return A remembered [TopAppBarState] initialized to the collapsed offset.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberCollapsedTopAppBarState(): TopAppBarState {
    val state = rememberTopAppBarState()
    var initialized by remember { mutableStateOf(false) }

    LaunchedEffect(state.heightOffsetLimit) {
        if (!initialized && state.heightOffsetLimit < 0f) {
            state.heightOffset = state.heightOffsetLimit
            initialized = true
        }
    }

    return state
}
