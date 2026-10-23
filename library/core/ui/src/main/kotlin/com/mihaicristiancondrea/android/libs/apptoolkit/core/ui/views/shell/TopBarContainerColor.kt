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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.shell

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.FrameTint

/**
 * How far the app bar above is tinted by the content scrolled under it, provided by `PageScaffold`
 * and the shell's tabs. Null outside them, under a bar that does not react to scrolling, and
 * where the bar is see-through over a tinted frame.
 */
val LocalTopBarTint = staticCompositionLocalOf<(() -> FrameTint)?> { null }

/**
 * The colour the app bar above is drawn in right now, for what sits flush under it, such as a tab
 * row, so the bar and the tabs read as one surface and change colour together as content scrolls
 * under them:
 *
 * ```
 * PrimaryTabRow(selectedTabIndex = page, containerColor = topBarContainerColor()) { ... }
 * ```
 *
 * It follows the bar the way Material tints it: a large bar blends as it collapses, the others
 * ease to the scrolled colour once content passes under them. Where nothing provides
 * [LocalTopBarTint] it is the bar's resting colour.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun topBarContainerColor(): Color {
    val colors = TopAppBarDefaults.topAppBarColors()
    val tint = LocalTopBarTint.current ?: return colors.containerColor
    val target by remember(tint) { derivedStateOf(tint) }
    val amount by animateFloatAsState(
        targetValue = target.amount,
        animationSpec = if (target.snap) snap() else MaterialTheme.motionScheme.defaultEffectsSpec(),
        label = "TopBarContainerColor",
    )
    return lerp(colors.containerColor, colors.scrolledContainerColor, FastOutLinearInEasing.transform(amount))
}
