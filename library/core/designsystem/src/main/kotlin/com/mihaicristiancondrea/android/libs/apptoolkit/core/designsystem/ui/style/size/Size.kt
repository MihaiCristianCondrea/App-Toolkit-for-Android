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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.size

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default values for [Size].
 *
 * The steps follow the icon sizes of Material 3's icon buttons, from 16dp to 64dp. Use them for
 * images, avatars and decorative graphics. An icon inside a Material component keeps that
 * component's own token, such as `ButtonDefaults.IconSize` or `IconButtonDefaults.mediumIconSize`,
 * and a plain `Icon` keeps its 24dp default.
 */
object SizeDefaults {
    /** 16dp. */
    val ExtraSmall: Dp = 16.dp

    /** 20dp. */
    val Small: Dp = 20.dp

    /** 24dp, the standard Material icon size. */
    val Medium: Dp = 24.dp

    /** 32dp. */
    val Large: Dp = 32.dp

    /** 40dp. */
    val ExtraLarge: Dp = 40.dp

    /** 48dp, the minimum touch target. */
    val ExtraExtraLarge: Dp = 48.dp

    /** 64dp. */
    val Huge: Dp = 64.dp
}

/**
 * Size scale for images, avatars and other graphics, read through [MaterialTheme.size] in the same
 * way as `MaterialTheme.shapes`. Each step can be overridden on its own for a subtree with
 * `CompositionLocalProvider(LocalSize provides Size(medium = 28.dp))`.
 *
 * A size outside the scale belongs in the owning module's `ui/constants`.
 */
@Immutable
data class Size(
    val extraSmall: Dp = SizeDefaults.ExtraSmall,
    val small: Dp = SizeDefaults.Small,
    val medium: Dp = SizeDefaults.Medium,
    val large: Dp = SizeDefaults.Large,
    val extraLarge: Dp = SizeDefaults.ExtraLarge,
    val extraExtraLarge: Dp = SizeDefaults.ExtraExtraLarge,
    val huge: Dp = SizeDefaults.Huge,
)

/** The active [Size]. Defaults to [Size] with every step at its [SizeDefaults] value. */
val LocalSize: ProvidableCompositionLocal<Size> = staticCompositionLocalOf { Size() }

/** The [Size] at the call site's position in the hierarchy. */
@Suppress("UnusedReceiverParameter")
val MaterialTheme.size: Size
    @Composable
    @ReadOnlyComposable
    get() = LocalSize.current
