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

package com.d4rk.android.libs.apptoolkit.core.ui.views.ads

import androidx.annotation.ColorInt
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb

/**
 * An ARGB snapshot of the Compose theme, for the Android views that render an ad.
 *
 * Ad assets have to live inside a real `NativeAdView`, which is a `View` and cannot read
 * [MaterialTheme]. Snapshotting the colours the ad needs keeps the ad visually part of the app —
 * including dynamic colour and in-app theme switches — without duplicating the palette in XML
 * themes.
 */
@Immutable
data class NativeAdPalette(
    @get:ColorInt val surface: Int,
    @get:ColorInt val surfaceContainer: Int,
    @get:ColorInt val onSurface: Int,
    @get:ColorInt val onSurfaceVariant: Int,
    @get:ColorInt val primary: Int,
    @get:ColorInt val secondaryContainer: Int,
    @get:ColorInt val onSecondaryContainer: Int,
    @get:ColorInt val surfaceContainerHighest: Int,
)

/**
 * Captures the current [MaterialTheme] colours as a [NativeAdPalette].
 *
 * The result is applied in the `AndroidView` *update* block rather than its factory, so an in-app
 * theme change that does not recreate the activity repaints the ad instead of leaving stale colours
 * behind.
 */
@Composable
fun nativeAdPalette(): NativeAdPalette {
    val colorScheme = MaterialTheme.colorScheme
    return remember(colorScheme) {
        NativeAdPalette(
            surface = colorScheme.surface.toArgb(),
            surfaceContainer = colorScheme.surfaceContainer.toArgb(),
            onSurface = colorScheme.onSurface.toArgb(),
            onSurfaceVariant = colorScheme.onSurfaceVariant.toArgb(),
            primary = colorScheme.primary.toArgb(),
            secondaryContainer = colorScheme.secondaryContainer.toArgb(),
            onSecondaryContainer = colorScheme.onSecondaryContainer.toArgb(),
            surfaceContainerHighest = colorScheme.surfaceContainerHighest.toArgb(),
        )
    }
}
