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

package com.d4rk.android.libs.apptoolkit.app.onboarding.ui.views.pages.theme.previews

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun SystemModePreview(modifier: Modifier = Modifier) {
    val lightBg = Color(0xFFF2F4F8)
    val darkBg = Color(0xFF111318)

    MiniUiPreview(
        modifier = modifier,
        background = Brush.horizontalGradient(
            colorStops = arrayOf(
                0.0f to lightBg,
                0.50f to lightBg,
                0.50f to darkBg,
                1.0f to darkBg
            )
        ),
        surface = Color.White.copy(alpha = 0.72f),
        line = Color(0xFF1B1F2A).copy(alpha = 0.18f),
        accent = MaterialTheme.colorScheme.primary,
    )
}