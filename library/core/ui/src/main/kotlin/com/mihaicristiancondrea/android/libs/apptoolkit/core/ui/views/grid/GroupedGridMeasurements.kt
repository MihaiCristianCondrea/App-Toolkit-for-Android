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
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.designsystem.ui.style.spacing.SpacingDefaults
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdBadgeShape
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdCallToActionStyle
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdStyle
import kotlin.math.roundToInt

/**
 * The five size classes a [GroupedGrid] cell can be drawn at.
 *
 * A caller picks a size the way it picks one for a button, and everything else a cell needs at that
 * size follows from the entry rather than being restated at the call site: icon badge, glyph,
 * content padding, the gap after the badge, and title/subtitle typography.
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
        GroupedGridMeasurements.ExtraSmall -> 56.dp
        GroupedGridMeasurements.Small -> 64.dp
        GroupedGridMeasurements.Medium -> 80.dp
        GroupedGridMeasurements.Large -> 96.dp
        GroupedGridMeasurements.ExtraLarge -> 112.dp
    }

val GroupedGridMeasurements.iconContainerSize: Dp
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> 32.dp
        GroupedGridMeasurements.Small -> 40.dp
        GroupedGridMeasurements.Medium -> 48.dp
        GroupedGridMeasurements.Large -> 56.dp
        GroupedGridMeasurements.ExtraLarge -> 64.dp
    }

internal val GroupedGridMeasurements.iconSize: Dp
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> 18.dp
        GroupedGridMeasurements.Small -> 20.dp
        GroupedGridMeasurements.Medium -> 24.dp
        GroupedGridMeasurements.Large -> 28.dp
        GroupedGridMeasurements.ExtraLarge -> 32.dp
    }

internal val GroupedGridMeasurements.contentPadding: PaddingValues
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> PaddingValues(
            horizontal = SpacingDefaults.Medium,
            vertical = SpacingDefaults.Small,
        )

        GroupedGridMeasurements.Small -> PaddingValues(all = SpacingDefaults.Medium)
        GroupedGridMeasurements.Medium -> PaddingValues(all = SpacingDefaults.Large)
        GroupedGridMeasurements.Large -> PaddingValues(all = SpacingDefaults.Large)
        GroupedGridMeasurements.ExtraLarge -> PaddingValues(all = SpacingDefaults.ExtraLargeCompact)
    }

internal val GroupedGridMeasurements.iconSpacing: Dp
    get() = when (this) {
        GroupedGridMeasurements.ExtraSmall -> SpacingDefaults.Small
        GroupedGridMeasurements.Small -> SpacingDefaults.Medium
        GroupedGridMeasurements.Medium -> SpacingDefaults.Medium
        GroupedGridMeasurements.Large -> SpacingDefaults.Large
        GroupedGridMeasurements.ExtraLarge -> SpacingDefaults.Large
    }

@Composable
internal fun GroupedGridMeasurements.titleTextStyle(): TextStyle = when (this) {
    GroupedGridMeasurements.ExtraSmall -> MaterialTheme.typography.labelLarge
    GroupedGridMeasurements.Small -> MaterialTheme.typography.titleSmall
    GroupedGridMeasurements.Medium -> MaterialTheme.typography.titleMedium
    GroupedGridMeasurements.Large -> MaterialTheme.typography.titleLarge
    GroupedGridMeasurements.ExtraLarge -> MaterialTheme.typography.headlineSmall
}

@Composable
internal fun GroupedGridMeasurements.subtitleTextStyle(): TextStyle = when (this) {
    GroupedGridMeasurements.ExtraSmall -> MaterialTheme.typography.labelSmall
    GroupedGridMeasurements.Small -> MaterialTheme.typography.bodySmall
    GroupedGridMeasurements.Medium -> MaterialTheme.typography.bodySmall
    GroupedGridMeasurements.Large -> MaterialTheme.typography.bodyMedium
    GroupedGridMeasurements.ExtraLarge -> MaterialTheme.typography.bodyMedium
}

/**
 * The ad row that belongs in a grid drawn at this size class.
 *
 * The row is measured against the cells rather than against the other ad surfaces: same badge size,
 * same icon inset, same padding and gap, so a sponsored row lines up with the rows above and below
 * it instead of announcing itself by being bigger. Its colours and text come from
 * [nativeAdStyle].
 */
internal fun GroupedGridMeasurements.nativeAdPresentation(): NativeAdPresentation.GridRow =
    NativeAdPresentation.GridRow(
        iconSizeDp = iconContainerSize.value.roundToInt(),
        iconInsetDp = ((iconContainerSize - iconSize) / 2f).value.roundToInt(),
        contentPaddingDp = contentPadding.calculateTopPadding().value.roundToInt(),
        iconSpacingDp = iconSpacing.value.roundToInt(),
    )

/**
 * How the ad row is finished so it reads as one of the cells.
 *
 * The badge takes the grid's silhouette and its icon colour rather than the neutral surface an ad
 * gets on its own, the headline takes the cells' title size, and the call to action is a text
 * button, because a filled pill inside a block of quiet rows is the loudest thing on the screen.
 */
internal fun GroupedGridMeasurements.nativeAdStyle(
    titleTextStyle: TextStyle,
    colors: GroupedGridColors,
    badgeShape: NativeAdBadgeShape?,
): NativeAdStyle = NativeAdStyle(
    badgeShape = badgeShape,
    badgeCornerRadiusDp = (iconContainerSize / AD_BADGE_RADIUS_DIVISOR).value.roundToInt(),
    badgeColor = colors.iconContainerColor,
    headlineTextSizeSp = titleTextStyle.fontSize
        .takeIf { it.type == TextUnitType.Sp }
        ?.value
        ?: DEFAULT_AD_HEADLINE_TEXT_SIZE_SP,
    headlineColor = colors.contentColor,
    bodyColor = colors.subtitleColor,
    callToAction = NativeAdCallToActionStyle.Text,
)

private const val AD_BADGE_RADIUS_DIVISOR: Float = 4f
private const val DEFAULT_AD_HEADLINE_TEXT_SIZE_SP: Float = 16f
