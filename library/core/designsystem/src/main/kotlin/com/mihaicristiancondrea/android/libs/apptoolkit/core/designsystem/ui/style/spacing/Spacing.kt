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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default values for [Spacing].
 *
 * Material 3 has no spacing tokens, so the names follow its shape corner scale
 * (`MaterialTheme.shapes`): [None], [ExtraSmall], [Small], [Medium], [Large], [LargeIncreased],
 * [ExtraLarge], [ExtraLargeIncreased] and [ExtraExtraLarge] have the same dp value as the corner of
 * the same name. The other steps fill the gaps between them: `Increased` is the step just above
 * its base name, `Compact` the step just below, and [ExtraTiny], [Huge] and [ExtraHuge] extend the
 * scale at both ends.
 */
object SpacingDefaults {
    /** 0dp, for an explicit absence of spacing. */
    val None: Dp = 0.dp

    /** 2dp, for hairline gaps and the seams between grouped rows. */
    val ExtraTiny: Dp = 2.dp

    /** 4dp, as `ShapeDefaults.ExtraSmall`. */
    val ExtraSmall: Dp = 4.dp

    /** 6dp, between [ExtraSmall] and [Small]. */
    val ExtraSmallIncreased: Dp = 6.dp

    /** 8dp, as `ShapeDefaults.Small`. */
    val Small: Dp = 8.dp

    /** 10dp, between [Small] and [Medium]. */
    val SmallIncreased: Dp = 10.dp

    /** 12dp, as `ShapeDefaults.Medium`. */
    val Medium: Dp = 12.dp

    /** 14dp, between [Medium] and [Large]. */
    val MediumIncreased: Dp = 14.dp

    /** 16dp, as `ShapeDefaults.Large`. */
    val Large: Dp = 16.dp

    /** 20dp, as `ShapeDefaults.LargeIncreased`. */
    val LargeIncreased: Dp = 20.dp

    /** 24dp, between [LargeIncreased] and [ExtraLarge]. */
    val ExtraLargeCompact: Dp = 24.dp

    /** 28dp, as `ShapeDefaults.ExtraLarge`. */
    val ExtraLarge: Dp = 28.dp

    /** 32dp, as `ShapeDefaults.ExtraLargeIncreased`. */
    val ExtraLargeIncreased: Dp = 32.dp

    /** 40dp, between [ExtraLargeIncreased] and [ExtraExtraLarge]. */
    val ExtraExtraLargeCompact: Dp = 40.dp

    /** 48dp, as `ShapeDefaults.ExtraExtraLarge`. */
    val ExtraExtraLarge: Dp = 48.dp

    /** 56dp, between [ExtraExtraLarge] and [Huge]. */
    val ExtraExtraLargeIncreased: Dp = 56.dp

    /** 64dp. */
    val Huge: Dp = 64.dp

    /** 72dp. */
    val ExtraHuge: Dp = 72.dp
}

/**
 * Spacing scale read through [MaterialTheme.spacing], in the same way `MaterialTheme.shapes` is
 * read. Each step can be overridden on its own for a subtree with
 * `CompositionLocalProvider(LocalSpacing provides Spacing(large = 18.dp))`.
 *
 * See [SpacingDefaults] for the default value of each step and how the names are chosen.
 */
@Immutable
data class Spacing(
    val none: Dp = SpacingDefaults.None,
    val extraTiny: Dp = SpacingDefaults.ExtraTiny,
    val extraSmall: Dp = SpacingDefaults.ExtraSmall,
    val extraSmallIncreased: Dp = SpacingDefaults.ExtraSmallIncreased,
    val small: Dp = SpacingDefaults.Small,
    val smallIncreased: Dp = SpacingDefaults.SmallIncreased,
    val medium: Dp = SpacingDefaults.Medium,
    val mediumIncreased: Dp = SpacingDefaults.MediumIncreased,
    val large: Dp = SpacingDefaults.Large,
    val largeIncreased: Dp = SpacingDefaults.LargeIncreased,
    val extraLargeCompact: Dp = SpacingDefaults.ExtraLargeCompact,
    val extraLarge: Dp = SpacingDefaults.ExtraLarge,
    val extraLargeIncreased: Dp = SpacingDefaults.ExtraLargeIncreased,
    val extraExtraLargeCompact: Dp = SpacingDefaults.ExtraExtraLargeCompact,
    val extraExtraLarge: Dp = SpacingDefaults.ExtraExtraLarge,
    val extraExtraLargeIncreased: Dp = SpacingDefaults.ExtraExtraLargeIncreased,
    val huge: Dp = SpacingDefaults.Huge,
    val extraHuge: Dp = SpacingDefaults.ExtraHuge,
)

/** The active [Spacing]. Defaults to [Spacing] with every step at its [SpacingDefaults] value. */
val LocalSpacing: ProvidableCompositionLocal<Spacing> = staticCompositionLocalOf { Spacing() }

/** The [Spacing] at the call site's position in the hierarchy. */
@Suppress("UnusedReceiverParameter")
val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current
