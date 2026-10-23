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

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import kotlinx.collections.immutable.ImmutableList

/** Posts queued messages as toasts when a sheet would cover the page's snackbar. Toasts have no actions. */
@Composable
fun MessageToastHost(
    messages: ImmutableList<UiMessage>,
    onShown: (id: Long) -> Unit,
    errorDuration: Int = Toast.LENGTH_LONG,
) {
    val context = LocalContext.current
    val currentOnShown by rememberUpdatedState(onShown)
    val next = messages.firstOrNull()

    LaunchedEffect(next?.id) {
        val message = next ?: return@LaunchedEffect
        Toast.makeText(
            context.applicationContext,
            message.text.asString(context),
            if (message.isError) errorDuration else Toast.LENGTH_SHORT,
        ).show()
        currentOnShown(message.id)
    }
}
