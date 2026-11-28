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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.theme.previews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.spacers.ExtraSmallVerticalSpacer

@Composable
fun MiniUiPreview(
    background: Brush,
    surface: Color,
    line: Color,
    accent: Color,
    modifier: Modifier = Modifier,
) {
    val deviceShape = MaterialTheme.shapes.largeIncreased.copy(
        bottomStart = ZeroCornerSize,
        bottomEnd = ZeroCornerSize,
    )

    val screenShape = RoundedCornerShape(
        topStart = 14.dp,
        topEnd = 14.dp,
        bottomStart = 0.dp,
        bottomEnd = 0.dp,
    )

    val isLight = surface.luminance() > 0.5f
    val deviceBodyColor = if (isLight) {
        Color.White.copy(alpha = 0.64f)
    } else {
        Color.Black.copy(alpha = 0.32f)
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        shape = deviceShape,
        color = deviceBodyColor,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = MaterialTheme.spacing.extraSmall,
                    end = MaterialTheme.spacing.extraSmall,
                    top = MaterialTheme.spacing.extraSmall,
                    bottom = MaterialTheme.spacing.none,
                )
                .clip(screenShape)
                .background(background)
                .padding(MaterialTheme.spacing.extraSmall),
        ) {
            ExtraSmallVerticalSpacer()
            PreviewPill(
                surface = surface,
                line = line,
                modifier = Modifier.height(12.dp)
            )
            ExtraSmallVerticalSpacer()
            PreviewRowItem(surface = surface, line = line, accent = accent)
            ExtraSmallVerticalSpacer()
            PreviewRowItem(surface = surface, line = line, accent = accent)
            ExtraSmallVerticalSpacer()
            PreviewRowItem(surface = surface, line = line, accent = accent, trailingDot = true)
        }
    }
}

@Composable
private fun PreviewPill(
    surface: Color,
    line: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(surface)
            .padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.extraSmall,
            ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
    ) {
        Spacer(
            Modifier
                .height(8.dp)
                .width(32.dp)
                .clip(CircleShape)
                .background(line.copy(alpha = 0.9f))
        )
        Spacer(
            Modifier
                .height(8.dp)
                .width(20.dp)
                .clip(CircleShape)
                .background(line.copy(alpha = 0.6f))
        )
    }
}

@Composable
private fun PreviewRowItem(
    surface: Color,
    line: Color,
    accent: Color,
    trailingDot: Boolean = false,
) {
    Row(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(surface)
            .padding(
                horizontal = MaterialTheme.spacing.small,
                vertical = MaterialTheme.spacing.extraSmallIncreased,
            ),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
    ) {
        Spacer(
            Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(accent)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            modifier = Modifier.weight(1f)
        ) {
            Spacer(
                Modifier
                    .height(4.dp)
                    .width(72.dp)
                    .clip(CircleShape)
                    .background(line.copy(alpha = 0.95f))
            )
            Spacer(
                Modifier
                    .height(4.dp)
                    .width(44.dp)
                    .clip(CircleShape)
                    .background(line.copy(alpha = 0.65f))
            )
        }

        if (trailingDot) {
            Spacer(
                Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(line.copy(alpha = 0.75f))
            )
        }
    }
}

