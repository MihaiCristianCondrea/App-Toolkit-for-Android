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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.R
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.fab.AnimatedExtendedFloatingActionButton

/**
 * Connects the Apps tab's floating action button to the list it acts on.
 *
 * The shell draws a tab's button outside the tab's content, so the list registers what the button
 * does here, through `AppsListScreen`'s `onRegisterRandomAppHandler`, and clears it while there is
 * no app to open.
 */
@Stable
class RandomAppAction {
    var handler: (() -> Unit)? by mutableStateOf(null)
}

/** Opens a random app from the list; shown only while the list has one to open. */
@Composable
fun RandomAppFloatingActionButton(
    action: RandomAppAction,
    modifier: Modifier = Modifier,
    expanded: Boolean = true,
) {
    val handler = action.handler
    val label = stringResource(id = R.string.open_random_app)
    AnimatedExtendedFloatingActionButton(
        modifier = modifier,
        visible = handler != null,
        enabled = handler != null,
        onClick = { handler?.invoke() },
        icon = {
            Icon(
                imageVector = Icons.Outlined.Casino,
                contentDescription = label,
            )
        },
        text = {
            Text(text = label)
        },
        expanded = expanded,
    )
}
