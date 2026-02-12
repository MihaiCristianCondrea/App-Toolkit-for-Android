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
fun LightModePreview(modifier: Modifier = Modifier) {
    MiniUiPreview(
        modifier = modifier,
        background = Brush.linearGradient(listOf(Color(0xFFF4F6FA), Color(0xFFEFF2F7))),
        surface = Color.White.copy(alpha = 0.92f),
        line = Color(0xFF1B1F2A).copy(alpha = 0.20f),
        accent = MaterialTheme.colorScheme.primary,
    )
}