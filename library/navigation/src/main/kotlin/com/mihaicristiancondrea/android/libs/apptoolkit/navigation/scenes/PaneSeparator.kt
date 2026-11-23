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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.scenes

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.R

/**
 * The handle between the panes. It widens while dragged or hovered, the way Material's pane
 * expansion handle does, so the person can see what they are holding.
 */
@Composable
internal fun PaneSeparator(interactions: MutableInteractionSource, modifier: Modifier) {
    val dragged by interactions.collectIsDraggedAsState()
    val hovered by interactions.collectIsHoveredAsState()
    val handleWidth by animateDpAsState(if (dragged || hovered) 12.dp else 4.dp, label = "HandleWidth")
    val description = stringResource(R.string.shell_pane_separator)
    Box(
        modifier = modifier
            .width(SeparatorWidth)
            .fillMaxHeight()
            .hoverable(interactions)
            .semantics { contentDescription = description },
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .size(width = handleWidth, height = 48.dp)
                .background(
                    if (dragged) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline,
                    CircleShape,
                ),
        )
    }
}

/** The width of the handle between the panes. */
internal val SeparatorWidth = 24.dp
