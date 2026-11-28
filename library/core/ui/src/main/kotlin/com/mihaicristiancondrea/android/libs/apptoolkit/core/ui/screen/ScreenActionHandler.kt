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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import androidx.compose.runtime.Composable

/**
 * Connects UI actions from [content] to a screen's action handler and event dispatcher.
 *
 * [handleAction] performs UI work and can call the supplied [onEvent] to send a ViewModel event.
 */
@Composable
fun <Action, Event> ScreenActionHandler(
    onEvent: (Event) -> Unit,
    handleAction: (Action, (Event) -> Unit) -> Unit,
    content: @Composable (onAction: (Action) -> Unit) -> Unit,
) {
    content { action -> handleAction(action, onEvent) }
}
