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

package com.mihaicristiancondrea.android.libs.apptoolkit.shell.chrome

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.AnimatedToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.icons.ToolkitIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.buttons.AnimatedIconButtonDirection
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.graph.DrawerEntry

import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.R as CoreUiR

/**
 * The app bar's overflow button and menu, listing the graph's overflow entries. The button slides
 * in and out with the tab roots it belongs to, and turns a quarter while the menu is open, as the
 * Toolkit's Support menu does.
 */
@Composable
internal fun OverflowMenu(
    entries: List<DrawerEntry>,
    callbacks: NavigationCallbacks,
    visible: Boolean
) {
    if (entries.isEmpty()) return
    var open by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(if (open) 90f else 0f, label = "OverflowRotation")
    val feedback = rememberNavigationFeedback(haptic = true)
    Box {
        AnimatedIconButtonDirection(
            modifier = Modifier.graphicsLayer { rotationZ = rotation },
            visible = visible,
            fromRight = true,
            icon = ToolkitIcon.Vector(Icons.Outlined.MoreVert),
            contentDescription = stringResource(CoreUiR.string.content_description_more_options),
            onClick = { open = true },
        )
        DropdownMenu(
            expanded = open && visible,
            onDismissRequest = { open = false },
            shape = MaterialTheme.shapes.largeIncreased,
        ) {
            entries.forEach { entry ->
                val (label, icon) = entry.labelAndIcon() ?: run {
                    HorizontalDivider()
                    return@forEach
                }
                DropdownMenuItem(
                    text = { Text(stringResource(label)) },
                    leadingIcon = {
                        AnimatedToolkitIcon(icon = icon, clickCount = 0, contentDescription = null)
                    },
                    onClick = {
                        feedback()
                        open = false
                        callbacks.onEntryClick(entry)
                    },
                )
            }
        }
    }
}
