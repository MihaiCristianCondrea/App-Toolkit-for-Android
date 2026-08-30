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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.mappers

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ColorSwatchData
import com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.models.ColorTable
import java.util.Locale

internal fun ColorScheme.toAppColorTable(title: String): ColorTable =
    ColorTable(
        title = title,
        colors = listOf(
            ColorSwatchData("primary", primary),
            ColorSwatchData("on primary", onPrimary),
            ColorSwatchData("primary container", primaryContainer),
            ColorSwatchData("secondary", secondary),
            ColorSwatchData("tertiary", tertiary),
            ColorSwatchData("error", error),
            ColorSwatchData("background", background),
            ColorSwatchData("surface", surface),
            ColorSwatchData("surface variant", surfaceVariant),
            ColorSwatchData("outline", outline),
            ColorSwatchData("inverse surface", inverseSurface),
            ColorSwatchData("scrim", scrim),
        ),
    )

internal fun Color.toHexString(): String = String.format(Locale.US, "#%08X", toArgb())
