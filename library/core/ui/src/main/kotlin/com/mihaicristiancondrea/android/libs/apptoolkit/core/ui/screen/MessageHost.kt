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

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.CustomSnackbarVisuals
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalPageSnackbarHostState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.rememberPageSnackbarHostState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar.DefaultSnackbarHost
import kotlinx.collections.immutable.ImmutableList

/**
 * Shows [viewModel]'s queued messages, one at a time and in order, and tells it as each leaves.
 *
 * By default the messages show through the Toolkit scaffold around the screen, a page's frame or
 * the shell's tabs, which draws them above its bars and buttons. Given a host of its own, it draws
 * that host here.
 *
 * @param onAction Called when a message's action is pressed, before it is marked as shown.
 * @param drawHost Whether to draw [snackbarHostState] here. By default only when it is not the
 * scaffold's, which draws its own.
 */
@Composable
fun MessageHost(
    viewModel: ScreenViewModel<*, *>,
    snackbarHostState: SnackbarHostState = rememberPageSnackbarHostState(),
    onAction: (UiMessage) -> Unit = {},
    drawHost: Boolean = snackbarHostState !== LocalPageSnackbarHostState.current,
) {
    val messages: ImmutableList<UiMessage> by viewModel.messages.collectAsStateWithLifecycle()
    MessageHost(
        messages = messages,
        onShown = viewModel::messageShown,
        snackbarHostState = snackbarHostState,
        onAction = onAction,
        drawHost = drawHost,
    )
}

/**
 * Shows [messages] one at a time, oldest first, calling [onShown] with each one's id once it has
 * left. A message still on screen when this leaves composition is not marked as shown, so it shows
 * again next time rather than being lost.
 */
@Composable
fun MessageHost(
    messages: ImmutableList<UiMessage>,
    onShown: (id: Long) -> Unit,
    snackbarHostState: SnackbarHostState = rememberPageSnackbarHostState(),
    onAction: (UiMessage) -> Unit = {},
    drawHost: Boolean = snackbarHostState !== LocalPageSnackbarHostState.current,
) {
    val context: Context = LocalContext.current
    val currentOnShown by rememberUpdatedState(onShown)
    val currentOnAction by rememberUpdatedState(onAction)
    val next: UiMessage? = messages.firstOrNull()

    LaunchedEffect(next?.id) {
        val message: UiMessage = next ?: return@LaunchedEffect
        val actionLabel: String? = message.actionLabel?.asString(context)
        val result: SnackbarResult = snackbarHostState.showSnackbar(
            visuals = CustomSnackbarVisuals(
                message = message.text.asString(context),
                actionLabel = actionLabel,
                withDismissAction = true,
                duration = if (message.isError || actionLabel != null) SnackbarDuration.Long else SnackbarDuration.Short,
                isError = message.isError,
            ),
        )
        if (result == SnackbarResult.ActionPerformed) currentOnAction(message)
        currentOnShown(message.id)
    }

    // A Toolkit scaffold already draws its own host; a second one here would show every message twice.
    if (drawHost) {
        DefaultSnackbarHost(snackbarState = snackbarHostState)
    }
}
