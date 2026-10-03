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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * The snackbar host of the page frame a screen is drawn in, or null outside one.
 *
 * `PageScaffold` draws this host in its snackbar slot, above the system bars and any floating
 * action button, so a screen shows its messages there rather than drawing a host of its own.
 */
val LocalPageSnackbarHostState = staticCompositionLocalOf<SnackbarHostState?> { null }

/** The page frame's snackbar host, or a new one remembered here for a screen outside a page. */
@Composable
fun rememberPageSnackbarHostState(): SnackbarHostState =
    LocalPageSnackbarHostState.current ?: remember { SnackbarHostState() }
