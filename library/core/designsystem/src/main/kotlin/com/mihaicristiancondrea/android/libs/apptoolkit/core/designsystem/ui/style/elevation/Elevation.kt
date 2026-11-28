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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.elevation

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Default values for [Elevation]: Material 3's six elevation levels, which Compose Material keeps
 * internal. A component's own default, such as `CardDefaults.cardElevation()`, comes first; these
 * levels are for custom surfaces and for a state a component has no default for.
 */
object ElevationDefaults {
    /** 0dp. */
    val Level0: Dp = 0.dp

    /** 1dp. */
    val Level1: Dp = 1.dp

    /** 3dp. */
    val Level2: Dp = 3.dp

    /** 6dp. */
    val Level3: Dp = 6.dp

    /** 8dp. */
    val Level4: Dp = 8.dp

    /** 12dp. */
    val Level5: Dp = 12.dp
}

/**
 * Elevation levels read through [MaterialTheme.elevation], for tonal and shadow elevation. Each
 * level can be overridden on its own with `CompositionLocalProvider(LocalElevation provides ...)`.
 */
@Immutable
data class Elevation(
    val level0: Dp = ElevationDefaults.Level0,
    val level1: Dp = ElevationDefaults.Level1,
    val level2: Dp = ElevationDefaults.Level2,
    val level3: Dp = ElevationDefaults.Level3,
    val level4: Dp = ElevationDefaults.Level4,
    val level5: Dp = ElevationDefaults.Level5,
)

/** The active [Elevation]. Defaults to [Elevation] with every level at its [ElevationDefaults] value. */
val LocalElevation: ProvidableCompositionLocal<Elevation> = staticCompositionLocalOf { Elevation() }

/** The [Elevation] at the call site's position in the hierarchy. */
@Suppress("UnusedReceiverParameter")
val MaterialTheme.elevation: Elevation
    @Composable
    @ReadOnlyComposable
    get() = LocalElevation.current
