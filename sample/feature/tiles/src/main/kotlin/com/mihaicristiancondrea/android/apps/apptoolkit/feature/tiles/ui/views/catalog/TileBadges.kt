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


package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.catalog

import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.badgeShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.lerp
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.iconColors
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.imageVector
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTileIcon

/**
 * A tool's badge, as the kit's `shapeBadge` draws it: the icon in the tool's accent, on the accent
 * mixed 26% into the highest surface container, cut from the tool's badge shape.
 */
@Composable
internal fun TileIconBadge(
    icon: ToolkitTileIcon,
    large: Boolean = false,
) {
    val colors = icon.iconColors()
    Box(
        modifier = Modifier
            .size(if (large) 64.dp else 44.dp)
            .clip(shape = icon.badgeShape())
            .background(color = lerp(MaterialTheme.colorScheme.surfaceContainerHighest, colors.container, 0.26f)),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            imageVector = icon.imageVector(),
            contentDescription = null,
            tint = colors.content,
            modifier = Modifier.size(if (large) 32.dp else 24.dp),
        )
    }
}
