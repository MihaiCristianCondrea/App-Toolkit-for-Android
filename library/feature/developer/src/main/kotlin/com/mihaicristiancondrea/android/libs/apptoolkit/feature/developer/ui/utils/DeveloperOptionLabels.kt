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

package com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.mihaicristiancondrea.android.libs.apptoolkit.feature.developer.R
import com.mihaicristiancondrea.android.libs.apptoolkit.navigation.layout.ShellLayoutMode

@Composable
internal fun layoutModeLabel(mode: ShellLayoutMode): String = stringResource(
    when (mode) {
        ShellLayoutMode.Auto -> R.string.shell_layout_auto
        ShellLayoutMode.BottomBar -> R.string.shell_layout_bottom_bar
        ShellLayoutMode.Rail -> R.string.shell_layout_rail
        ShellLayoutMode.ExpandedRail -> R.string.shell_layout_expanded_rail
        ShellLayoutMode.PermanentDrawer -> R.string.shell_layout_permanent_drawer
    },
)
