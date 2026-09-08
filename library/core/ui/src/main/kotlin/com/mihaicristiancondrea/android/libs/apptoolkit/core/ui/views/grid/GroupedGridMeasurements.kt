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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.grid

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants

/**
 * The five size classes a [GroupedGrid] cell can be drawn at.
 *
 * A caller picks a size the way it picks one for a button, and everything else a cell needs at that
 * size — icon badge, glyph, content padding, the gap after the badge, and title/subtitle
 * typography — follows from the entry rather than being restated at the call site.
 *
 * [Medium] is the default because it is the size the storage and media breakdowns were drawn at
 * before this component existed: a 48dp badge inside 16dp of padding.
 */
enum class GroupedGridMeasurements {
    ExtraSmall,
    Small,
    Medium,
    Large,
    ExtraLarge,
}

/**
 * Minimum height of a cell at this size class.
 *
 * It is a minimum rather than a fixed height: a title that wraps grows its own cell, and the row it
 * sits in matches the tallest cell in it. Callers aligning a sibling composable with a grid should
 * measure against this value.
 */
val GroupedGridMeasurements.cellHeight: Dp
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> SizeConstants.LauncherIconSize + SizeConstants.SmallSize
        GroupedGridMeasurements.Small -> SizeConstants.LauncherIconSize + SizeConstants.LargeSize
        GroupedGridMeasurements.Medium -> SizeConstants.EightySize
        GroupedGridMeasurements.Large -> SizeConstants.NinetySixSize
        GroupedGridMeasurements.ExtraLarge -> SizeConstants.NinetySixSize + SizeConstants.LargeSize
    }

/** Size of the badge drawn behind a cell's icon at this size class. */
val GroupedGridMeasurements.iconContainerSize: Dp
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> SizeConstants.ExtraLargeIncreasedSize
        GroupedGridMeasurements.Small -> SizeConstants.ExtraLargeSize + SizeConstants.MediumSize
        GroupedGridMeasurements.Medium -> SizeConstants.LauncherIconSize
        GroupedGridMeasurements.Large -> SizeConstants.LauncherIconSize + SizeConstants.SmallSize
        GroupedGridMeasurements.ExtraLarge -> SizeConstants.LauncherIconSize + SizeConstants.LargeSize
    }

/** Size of the glyph inside the badge at this size class. */
internal val GroupedGridMeasurements.iconSize: Dp
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> SizeConstants.LargeMediumSize
        GroupedGridMeasurements.Small -> SizeConstants.LargeIncreasedSize
        GroupedGridMeasurements.Medium -> SizeConstants.TwentyFourSize
        GroupedGridMeasurements.Large -> SizeConstants.ExtraLargeSize
        GroupedGridMeasurements.ExtraLarge -> SizeConstants.ExtraLargeIncreasedSize
    }

/** Inset between a cell's edges and its content at this size class. */
internal val GroupedGridMeasurements.contentPadding: PaddingValues
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> PaddingValues(
            horizontal = SizeConstants.MediumSize,
            vertical = SizeConstants.SmallSize,
        )

        GroupedGridMeasurements.Small -> PaddingValues(all = SizeConstants.MediumSize)
        GroupedGridMeasurements.Medium -> PaddingValues(all = SizeConstants.LargeSize)
        GroupedGridMeasurements.Large -> PaddingValues(all = SizeConstants.LargeSize)
        GroupedGridMeasurements.ExtraLarge -> PaddingValues(all = SizeConstants.ExtraLargeCompactSize)
    }

/** Gap between the icon badge and the text column at this size class. */
internal val GroupedGridMeasurements.iconSpacing: Dp
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> SizeConstants.SmallSize
        GroupedGridMeasurements.Small -> SizeConstants.MediumSize
        GroupedGridMeasurements.Medium -> SizeConstants.MediumSize
        GroupedGridMeasurements.Large -> SizeConstants.LargeSize
        GroupedGridMeasurements.ExtraLarge -> SizeConstants.LargeSize
    }

/** Title typography at this size class. */
@Composable
internal fun GroupedGridMeasurements.titleTextStyle(): TextStyle = when (this) {
    GroupedGridMeasurements.ExtraSmall -> MaterialTheme.typography.labelLarge
    GroupedGridMeasurements.Small -> MaterialTheme.typography.titleSmall
    GroupedGridMeasurements.Medium -> MaterialTheme.typography.titleMedium
    GroupedGridMeasurements.Large -> MaterialTheme.typography.titleLarge
    GroupedGridMeasurements.ExtraLarge -> MaterialTheme.typography.headlineSmall
}

/** Subtitle typography at this size class. */
@Composable
internal fun GroupedGridMeasurements.subtitleTextStyle(): TextStyle = when (this) {
    GroupedGridMeasurements.ExtraSmall -> MaterialTheme.typography.labelSmall
    GroupedGridMeasurements.Small -> MaterialTheme.typography.bodySmall
    GroupedGridMeasurements.Medium -> MaterialTheme.typography.bodySmall
    GroupedGridMeasurements.Large -> MaterialTheme.typography.bodyMedium
    GroupedGridMeasurements.ExtraLarge -> MaterialTheme.typography.bodyMedium
}
