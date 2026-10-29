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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers.imageVector

/** Shared dimensions mirror the marketing bento groups; the shell owns all system insets. */
@Composable
internal fun ToolWorkspace(padding: PaddingValues, content: @Composable ColumnScope.() -> Unit) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(
        start = 16.dp, end = 16.dp, top = padding.calculateTopPadding() + 8.dp,
        bottom = padding.calculateBottomPadding() + 16.dp,
    ), verticalArrangement = Arrangement.spacedBy(24.dp), content = content)
}

@Composable
internal fun ToolSection(title: String, icon: ImageVector, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Row(Modifier.padding(start = 4.dp), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        content()
    }
}

internal fun toolCorners(index: Int, size: Int, top: Boolean = true, bottom: Boolean = true): Shape = RoundedCornerShape(
    topStart = if (top && index == 0) 28.dp else 8.dp,
    topEnd = if (top && index == size - 1) 28.dp else 8.dp,
    bottomStart = if (bottom && index == 0) 28.dp else 8.dp,
    bottomEnd = if (bottom && index == size - 1) 28.dp else 8.dp,
)

/**
 * A Measure card (the marketing kit's `.mw-widget`): the tool's name, then its own [content].
 * [headerAction] sits at the end of the name's row, as the counter's Reset does.
 */
@Composable
internal fun ToolWidget(tile: ToolkitTile, onOpen: () -> Unit, modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(28.dp), color: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    headerAction: @Composable (() -> Unit)? = null,
    padding: PaddingValues = PaddingValues(horizontal = 16.dp, vertical = 20.dp),
    spacing: Dp = 12.dp,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(onClick = onOpen, modifier = modifier, shape = shape, color = color) {
        Column(Modifier.padding(padding), verticalArrangement = Arrangement.spacedBy(spacing)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(tile.titleResId),
                    modifier = Modifier.weight(1f).padding(start = if (headerAction != null) 4.dp else 0.dp),
                    style = MaterialTheme.typography.titleMedium.copy(lineHeight = 22.sp),
                    fontWeight = FontWeight.SemiBold,
                )
                headerAction?.invoke()
            }
            content()
        }
    }
}

@Composable
internal fun ToolPair(tools: List<ToolkitTile>, top: Boolean = true, bottom: Boolean = true,
    content: @Composable RowScope.(ToolkitTile, Shape) -> Unit) {
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        tools.forEachIndexed { index, tile -> content(tile, toolCorners(index, tools.size, top, bottom)) }
    }
}
