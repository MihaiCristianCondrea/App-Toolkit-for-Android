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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.snackbar

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.base.handling.UiEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.CustomSnackbarVisuals
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiSnackbar
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.LocalPageSnackbarHostState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell.rememberPageSnackbarHostState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.states.UiStateScreen

/**
 * Shows the snackbar described by [UiStateScreen.snackbar], in the error or normal style its
 * [UiSnackbar.isError] picks, with its [UiSnackbar.actionLabel] as action.
 *
 * By default it shows through the Toolkit scaffold around the screen, a page's frame or the shell's
 * tabs, which draws it above its bars and buttons. Given a host of its own, it draws that host
 * here.
 *
 * @param screenState Screen state containing the snackbar information.
 * @param snackbarHostState Host state that renders the snackbar; the scaffold's by default.
 * @param getDismissEvent Factory for an event sent once the snackbar leaves, whether dismissed or
 * after its action.
 * @param onEvent Callback receiving the events created by [getDismissEvent] and [getActionEvent].
 * @param getActionEvent Factory for an event sent when the snackbar's action is performed, before
 * the dismiss event.
 * @param drawHost Whether to draw [snackbarHostState] here. By default only when it is not the
 * scaffold's, which draws its own; false for a host drawn elsewhere, such as the one an app hands
 * to `ShellHost`.
 */
@Composable
fun <T, E : UiEvent> DefaultSnackbarHandler(
    screenState: UiStateScreen<T>,
    snackbarHostState: SnackbarHostState = rememberPageSnackbarHostState(),
    getDismissEvent: (() -> E)? = null,
    onEvent: ((E) -> Unit)? = null,
    getActionEvent: (() -> E)? = null,
    drawHost: Boolean = snackbarHostState !== LocalPageSnackbarHostState.current,
) {
    val context: Context = LocalContext.current

    LaunchedEffect(key1 = screenState.snackbar?.timeStamp) {
        screenState.snackbar?.let { snackbar: UiSnackbar ->
            if (snackbarHostState.currentSnackbarData != null) {
                snackbarHostState.currentSnackbarData?.dismiss()
            }

            val actionLabel: String? = snackbar.actionLabel?.asString(context)
            val result: SnackbarResult = snackbarHostState.showSnackbar(
                visuals = CustomSnackbarVisuals(
                    message = snackbar.message.asString(context),
                    actionLabel = actionLabel,
                    withDismissAction = true,
                    duration = if (snackbar.isError || actionLabel != null) SnackbarDuration.Long else SnackbarDuration.Short,
                    isError = snackbar.isError
                )
            )
            if (onEvent != null) {
                if (result == SnackbarResult.ActionPerformed && getActionEvent != null) {
                    onEvent(getActionEvent())
                }
                if (getDismissEvent != null) onEvent(getDismissEvent())
            }
        }
    }

    // A Toolkit scaffold already draws its own host; a second one here would show every message twice.
    if (drawHost) {
        DefaultSnackbarHost(snackbarState = snackbarHostState)
    }
}
