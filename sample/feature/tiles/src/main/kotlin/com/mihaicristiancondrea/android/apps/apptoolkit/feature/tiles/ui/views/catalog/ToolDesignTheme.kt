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

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

/** Product accent roles, shared with the marketing preview. Host surface roles are preserved. */
internal val ToolLightColors = lightColorScheme(
    primary = Color(0xFF6B5E00),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFF0B3),
    onPrimaryContainer = Color(0xFF211B00),
    secondary = Color(0xFF665E40),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFF2E8C9),
    onSecondaryContainer = Color(0xFF211B04),
    tertiary = Color(0xFF79505F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD9E5),
    onTertiaryContainer = Color(0xFF30101E),
    background = Color(0xFFFFFBF5),
    onBackground = Color(0xFF1E1B16),
    surface = Color(0xFFFFFBF5),
    onSurface = Color(0xFF1E1B16),
    surfaceVariant = Color(0xFFEAE2CF),
    onSurfaceVariant = Color(0xFF4B4639),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFFAF5EA),
    surfaceContainer = Color(0xFFF4EFE4),
    surfaceContainerHigh = Color(0xFFEEE9DE),
    surfaceContainerHighest = Color(0xFFE8E3D8),
    surfaceBright = Color(0xFFFFFBF5),
    surfaceDim = Color(0xFFDED9CE),
    outline = Color(0xFF7C7667),
    outlineVariant = Color(0xFFCEC6B5),
    inverseSurface = Color(0xFF33302A),
    inverseOnSurface = Color(0xFFF7F0E5),
    inversePrimary = Color(0xFFE2C900),
    surfaceTint = Color(0xFF6B5E00),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
    scrim = Color(0xFF000000),
)
internal val ToolDarkColors = darkColorScheme(
    primary = Color(0xFFDCCB7A),
    onPrimary = Color(0xFF332C0D),
    primaryContainer = Color(0xFF3F3820),
    onPrimaryContainer = Color(0xFFF3E7B5),
    secondary = Color(0xFFD5CCAC),
    onSecondary = Color(0xFF302D20),
    secondaryContainer = Color(0xFF363226),
    onSecondaryContainer = Color(0xFFE8DDBE),
    tertiary = Color(0xFFE5B6CB),
    onTertiary = Color(0xFF3A2530),
    tertiaryContainer = Color(0xFF422F39),
    onTertiaryContainer = Color(0xFFF4D6E1),
    background = Color(0xFF16140F),
    onBackground = Color(0xFFE8E2D7),
    surface = Color(0xFF16140F),
    onSurface = Color(0xFFE8E2D7),
    surfaceVariant = Color(0xFF4B4639),
    onSurfaceVariant = Color(0xFFCEC6B5),
    surfaceContainerLowest = Color(0xFF100E0A),
    surfaceContainerLow = Color(0xFF1E1C17),
    surfaceContainer = Color(0xFF22201B),
    surfaceContainerHigh = Color(0xFF2D2A25),
    surfaceContainerHighest = Color(0xFF38352F),
    surfaceBright = Color(0xFF3D3933),
    surfaceDim = Color(0xFF16140F),
    outline = Color(0xFF969080),
    outlineVariant = Color(0xFF4B4639),
    inverseSurface = Color(0xFFE8E2D7),
    inverseOnSurface = Color(0xFF33302A),
    inversePrimary = Color(0xFF6B5E00),
    surfaceTint = Color(0xFFE2C900),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    scrim = Color(0xFF000000),
)

@Composable
internal fun ToolDesignTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = toolColorScheme(MaterialTheme.colorScheme), content = content)
}

/** Keep the app's backdrop and neutral surfaces while fixing the coloured tool roles. */
internal fun toolColorScheme(host: ColorScheme): ColorScheme {
    val accents = if (host.surface.luminance() < .5f) ToolDarkColors else ToolLightColors
    return host.copy(
        primary = accents.primary,
        onPrimary = accents.onPrimary,
        primaryContainer = accents.primaryContainer,
        onPrimaryContainer = accents.onPrimaryContainer,
        inversePrimary = accents.inversePrimary,
        secondary = accents.secondary,
        onSecondary = accents.onSecondary,
        secondaryContainer = accents.secondaryContainer,
        onSecondaryContainer = accents.onSecondaryContainer,
        tertiary = accents.tertiary,
        onTertiary = accents.onTertiary,
        tertiaryContainer = accents.tertiaryContainer,
        onTertiaryContainer = accents.onTertiaryContainer,
    )
}
