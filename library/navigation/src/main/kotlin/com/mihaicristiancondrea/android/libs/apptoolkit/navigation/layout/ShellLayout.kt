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

package com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberShellLayoutInfo(
    policy: ShellLayoutPolicy,
    requested: ShellLayoutMode,
    limitContentWidth: Boolean,
): ShellLayoutInfo {
    val size = LocalWindowInfo.current.containerSize
    val density = LocalDensity.current
    val width = with(density) { size.width.toDp() }
    val height = with(density) { size.height.toDp() }
    return ShellLayoutInfo(
        mode = policy.resolve(requested, width),
        windowWidth = width,
        windowHeight = height,
        listDetail = width >= policy.listDetailFrom,
        contentMaxWidth = if (limitContentWidth) policy.contentMaxWidth else Dp.Unspecified,
        allowsLargeTopBar = height >= policy.largeTopBarFrom,
    )
}

val LocalShellLayout = staticCompositionLocalOf {
    ShellLayoutInfo(ShellLayoutMode.BottomBar, 0.dp, 0.dp, listDetail = false)
}
