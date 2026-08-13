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
}
