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

    /** Full-width strip for a bottom app bar or action bar: icon, one-line text, trailing CTA. */
    data object BarRow : NativeAdPresentation

    /**
     * One row of a grouped grid: icon badge, headline, the disclosure chip inline with the body,
     * advertiser, and a trailing CTA, all on a single row, so the ad is no taller than the cells
     * it sits between.
     *
     * It is the only presentation whose metrics are chosen by the caller. A grid draws its cells at
     * a size class, and an ad row that ignored that size would read as a different kind of block:
     * the row takes the same badge, inset, padding and headline size as the cells around it so it
     * reads as one of them. Colors, the disclosure chip and the CTA stay with the shared renderer.
     *
     * Not a `data class`: [iconShape] is deliberately left out of equality. The renderer keys its
     * view tree on the presentation and applies the palette on every update, so the badge silhouette
     * is repainted rather than rebuilt. Were it compared by identity, a caller that rebuilt the
     * shape would tear down and restart the ad request underneath it.
     *
     * @property iconSizeDp Badge size, matching the cells' badge.
     * @property iconInsetDp Inset between the badge and the ad icon, so the icon is drawn at the
     *   size of the glyphs in the cells rather than filling the badge.
     * @property iconCornerRadiusDp Badge corner radius, used when no [iconShape] is supplied.
     * @property headlineTextSizeSp Headline size, taken from the cells' title style.
     * @property contentPaddingDp Inset from the row's edges, matching the cells' content padding.
     * @property iconSpacingDp Gap after the badge, matching the cells'.
     * @property iconShape Silhouette the badge is filled with. `null` falls back to a rounded square
     *   at [iconCornerRadiusDp].
     */
    @Immutable
    class GridRow(
        val iconSizeDp: Int,
        val iconInsetDp: Int,
        val iconCornerRadiusDp: Int,
        val headlineTextSizeSp: Float,
        val contentPaddingDp: Int,
        val iconSpacingDp: Int,
        val iconShape: NativeAdBadgeShape? = null,
    ) : NativeAdPresentation {

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is GridRow) return false
            return iconSizeDp == other.iconSizeDp &&
                    iconInsetDp == other.iconInsetDp &&
                    iconCornerRadiusDp == other.iconCornerRadiusDp &&
                    headlineTextSizeSp == other.headlineTextSizeSp &&
                    contentPaddingDp == other.contentPaddingDp &&
                    iconSpacingDp == other.iconSpacingDp
        }

        override fun hashCode(): Int {
            var result = iconSizeDp
            result = 31 * result + iconInsetDp
            result = 31 * result + iconCornerRadiusDp
            result = 31 * result + headlineTextSizeSp.hashCode()
            result = 31 * result + contentPaddingDp
            result = 31 * result + iconSpacingDp
            return result
        }
    }
}
