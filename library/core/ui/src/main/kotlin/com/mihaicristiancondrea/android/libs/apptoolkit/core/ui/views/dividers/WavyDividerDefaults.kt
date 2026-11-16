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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.dividers

import androidx.compose.foundation.layout.height
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Defaults for [HorizontalWavyDivider] and [VerticalWavyDivider], taken from `il_wavy_line`. */
object WavyDividerDefaults {

    /** The band the wave occupies across the line: the drawable's 8dp height. */
    val WaveSize: Dp = 8.dp

    /** The drawable's 1dp stroke. */
    val Thickness: Dp = 1.dp

    /** The colour of Material's plain dividers, so a wavy divider sits among them naturally. */
    val color: Color
        @Composable get() = DividerDefaults.color
}
