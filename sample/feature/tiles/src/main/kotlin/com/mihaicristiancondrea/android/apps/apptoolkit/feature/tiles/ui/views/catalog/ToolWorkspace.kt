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

import androidx.compose.foundation.layout.navigationBarsPadding
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.modifiers.animateVisibility

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
import androidx.compose.ui.unit.sp
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ToolkitTile
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size.size
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.spacing

/**
 * A tab's scrolling body. The viewport extends edge-to-edge so cards scroll behind the bottom bar,
 * banner, player and transparent system navigation bar, while [padding] reserves space inside the
 * scrollable content so the last card can still scroll clear of them.
 *
 * The bottom bar hides on scroll and takes the system navigation bar's inset with it, so while it
 * is hidden [padding] no longer covers the gesture or button bar. The shell consumes what its
 * padding covers, so `navigationBarsPadding` adds only the part of the system bar left uncovered:
 * nothing while the bottom bar shows, the whole bar once it has gone.
 */
@Composable
internal fun ToolWorkspace(
    padding: PaddingValues,
    footer: @Composable () -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(padding)
            .navigationBarsPadding()
            .padding(top = MaterialTheme.spacing.small, bottom = MaterialTheme.spacing.large),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLargeCompact),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.large),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraLargeCompact),
            content = content,
        )
        footer()
    }
}

@Composable
internal fun ToolSection(title: String, icon: ImageVector, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)) {
        Row(Modifier.animateVisibility().padding(start = MaterialTheme.spacing.extraSmall), verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)) {
            Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(size = MaterialTheme.size.small))
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        }
        content()
    }
}

@Composable
internal fun toolCorners(index: Int, size: Int, top: Boolean = true, bottom: Boolean = true): Shape {
    val outer = MaterialTheme.shapes.extraLarge
    val inner = MaterialTheme.shapes.small
    return RoundedCornerShape(
        topStart = if (top && index == 0) outer.topStart else inner.topStart,
        topEnd = if (top && index == size - 1) outer.topEnd else inner.topEnd,
        bottomStart = if (bottom && index == 0) outer.bottomStart else inner.bottomStart,
        bottomEnd = if (bottom && index == size - 1) outer.bottomEnd else inner.bottomEnd,
    )
}

/**
 * A Measure card (the marketing kit's `.mw-widget`): the tool's name, then its own [content].
 * [headerAction] sits at the end of the name's row, as the counter's Reset does.
 */
@Composable
internal fun ToolWidget(tile: ToolkitTile, onOpen: () -> Unit, modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.extraLarge, color: Color = MaterialTheme.colorScheme.surfaceContainerHigh,
    headerAction: @Composable (() -> Unit)? = null,
    padding: PaddingValues = PaddingValues(horizontal = MaterialTheme.spacing.large, vertical = MaterialTheme.spacing.largeIncreased),
    spacing: Dp = MaterialTheme.spacing.medium,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(onClick = onOpen, modifier = modifier.animateVisibility(), shape = shape, color = color) {
        Column(Modifier.padding(padding), verticalArrangement = Arrangement.spacedBy(spacing)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(tile.titleResId),
                    modifier = Modifier.weight(1f).padding(start = if (headerAction != null) MaterialTheme.spacing.extraSmall else MaterialTheme.spacing.none),
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
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min), horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)) {
        tools.forEachIndexed { index, tile -> content(tile, toolCorners(index, tools.size, top, bottom)) }
    }
}
