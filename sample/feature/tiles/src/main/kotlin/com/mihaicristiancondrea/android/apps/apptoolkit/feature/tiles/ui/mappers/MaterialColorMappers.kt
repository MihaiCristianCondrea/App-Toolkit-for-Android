/*
 * Copyright (Â©) 2026 Mihai-Cristian Condrea
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
