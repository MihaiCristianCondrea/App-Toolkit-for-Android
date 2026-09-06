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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.ui

import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.NavigationIcon
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.models.resolveNavigationIcon

/**
 * Renders the icon of a navigation item, mixing the selected/unselected icons with the
 * click driven playback of Animated Vector Drawables.
 *
 * Behaviour, per icon combination:
 * - Two static icons (vector or drawable resource): the icon is swapped on selection, nothing animates.
 * - Static unselected icon + AVD selected icon: the static icon is shown at rest; the first click
 *   swaps in the AVD and plays it forward. Every further click replays it (forward, then backwards,
 *   alternating), so a repeatedly clicked action such as *Share* animates every single time.
 * - A single AVD used for both states: the drawable rests on its first frame while unselected and on
 *   its last frame while selected, and still replays on every click.
 *
 * @param icon Icon shown while the item is unselected.
 * @param selectedIcon Icon shown while the item is selected.
 * @param selected Whether the item is the current destination.
 * @param clickCount Number of clicks the item received while composed. Incrementing it replays the
 *   animation; items that are never selected rely on this alone.
 * @param contentDescription Accessibility description of the icon.
 * @param modifier The [Modifier] to apply to the icon.
 * @param tint Tint color to apply to the icon, defaults to [LocalContentColor].
 */
@Composable
fun NavigationItemIcon(
    icon: NavigationIcon,
    selectedIcon: NavigationIcon,
    selected: Boolean,
    clickCount: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = LocalContentColor.current,
) {
    val displayedIcon: NavigationIcon = resolveNavigationIcon(
        icon = icon,
        selectedIcon = selectedIcon,
        selected = selected,
        interacted = clickCount > 0,
    )

    if (displayedIcon !is NavigationIcon.AnimatedVector) {
        NavigationIconContent(
            icon = displayedIcon,
            contentDescription = contentDescription,
            modifier = modifier,
            tint = tint,
        )
        return
    }

    val restingAtEnd: Boolean = selected || displayedIcon.atEnd
    var atEnd: Boolean by remember(displayedIcon.resId) { mutableStateOf(value = restingAtEnd) }
    var lastSelected: Boolean by remember(displayedIcon.resId) { mutableStateOf(value = selected) }

    LaunchedEffect(selected, clickCount) {
        // Let the painter draw the current frame once before flipping, otherwise the drawable is
        // created already at its target state and jumps instead of animating.
        withFrameNanos { }
        when {
            selected != lastSelected -> {
                lastSelected = selected
                atEnd = restingAtEnd
            }

            clickCount > 0 -> atEnd = !atEnd
        }
    }

    NavigationIconContent(
        icon = displayedIcon,
        contentDescription = contentDescription,
        modifier = modifier,
        atEnd = atEnd,
        tint = tint,
    )
}
