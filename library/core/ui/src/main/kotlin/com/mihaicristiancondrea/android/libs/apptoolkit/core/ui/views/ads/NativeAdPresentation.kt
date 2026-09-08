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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import androidx.compose.runtime.Immutable

/**
 * The shape a native ad takes on screen.
 *
 * A presentation is the unit of reuse for native ads: new ad surfaces add a presentation here rather
 * than a new component with its own view tree. Every presentation renders through the same
 * [NativeAdSlot] and the same programmatic `NativeAdView`, so ad policy, lifecycle, palette, and the
 * disclosure label stay in one place.
 */
@Immutable
sealed interface NativeAdPresentation {

    /** Media-led card: `MediaView` at 16:9, headline, body, then icon, advertiser and CTA. */
    data object Featured : NativeAdPresentation

    /** Icon-led row inside a card: icon, headline, body, advertiser, trailing CTA. */
    data object Compact : NativeAdPresentation

    /** Square cell for grids and decks: icon, headline, advertiser, body. No CTA. */
    data object Grid : NativeAdPresentation

    /**
     * One row of a grouped grid: icon badge, headline, the disclosure chip inline with the body,
     * advertiser, and a trailing CTA — all on a single row, so the ad is no taller than the cells
     * it sits between.
     *
     * It is the only presentation whose metrics are chosen by the caller. A grid draws its cells at
     * a size class, and an ad row that ignored that size would read as a different kind of block:
     * the row takes the same badge, padding and headline size as the cells around it so it reads as
     * one of them. Everything else — colors, the disclosure chip, the CTA — stays with the shared
     * renderer.
     *
     * @property iconSizeDp Badge size, matching the cells' badge.
     * @property iconCornerRadiusDp Badge corner radius. The cells cut their badge from an arbitrary
     *   `Shape`, which an Android view cannot follow, so the ad badge is a rounded square.
     * @property headlineTextSizeSp Headline size, taken from the cells' title style.
     * @property contentPaddingDp Inset from the row's edges, matching the cells' content padding.
     * @property iconSpacingDp Gap after the badge, matching the cells'.
     */
    data class GridRow(
        val iconSizeDp: Int,
        val iconCornerRadiusDp: Int,
        val headlineTextSizeSp: Float,
        val contentPaddingDp: Int,
        val iconSpacingDp: Int,
    ) : NativeAdPresentation

    /** Full-width strip for a bottom app bar or action bar: icon, one-line text, trailing CTA. */
    data object BarRow : NativeAdPresentation
}
