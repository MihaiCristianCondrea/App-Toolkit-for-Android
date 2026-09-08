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
import androidx.compose.ui.graphics.Color

/** How an ad's call to action is drawn. */
enum class NativeAdCallToActionStyle {
    /** A filled pill, which is the default and reads as the strongest action in the card. */
    Filled,

    /** A text button, for screens whose own actions are text buttons. */
    Text,
}

/**
 * Per-screen look for a native ad.
 *
 * A native ad only works when it looks like it belongs on the screen it is on, and screens differ:
 * one draws its icons in a circle, the next cuts them from a Material shape, one uses filled
 * buttons, the next uses text buttons. A style says how this one placement should look, without the
 * host having to build a view tree of its own.
 *
 * **It overrides only what it names.** Every value has a default that means "leave it as the
 * presentation built it", so a style that sets one property changes one property. That also makes
 * styles safe to combine with a custom [NativeAdViewFactory]: the factory decides the arrangement,
 * the style decides the finish.
 *
 * It is applied in the same pass as [NativeAdPalette], on views that already exist, so changing a
 * style repaints the ad rather than rebuilding it. Rebuilding would discard the loaded ad.
 *
 * @property badgeShape Silhouette the icon badge is filled with, from `rememberNativeAdBadgeShape`.
 *   `null` keeps the rounded square at [badgeCornerRadiusDp].
 * @property badgeCornerRadiusDp Corner radius of the badge when there is no [badgeShape]. `null`
 *   keeps the shared radius.
 * @property badgeColor Badge fill. [Color.Unspecified] keeps the palette's neutral surface, which
 *   is right for a card that sits on its own and wrong on a screen whose icons are all
 *   `primaryContainer`.
 * @property headlineTextSizeSp Headline size. `null` keeps the presentation's own.
 * @property headlineBold Whether the headline is bold. `null` keeps the presentation's own. A
 *   headline heavier than the titles around it reads as an intruder.
 * @property headlineColor Headline color. [Color.Unspecified] keeps the palette's `onSurface`.
 * @property bodyTextSizeSp Body size. `null` keeps the presentation's own.
 * @property bodyMaxLines Body line cap. `null` keeps the presentation's own. Lower it when a long
 *   description would make the ad taller than the rows beside it.
 * @property bodyColor Body color. [Color.Unspecified] keeps the palette's `onSurfaceVariant`.
 * @property callToAction Whether the call to action is a filled pill or a text button.
 */
@Immutable
data class NativeAdStyle(
    val badgeShape: NativeAdBadgeShape? = null,
    val badgeCornerRadiusDp: Int? = null,
    val badgeColor: Color = Color.Unspecified,
    val headlineTextSizeSp: Float? = null,
    val headlineBold: Boolean? = null,
    val headlineColor: Color = Color.Unspecified,
    val bodyTextSizeSp: Float? = null,
    val bodyMaxLines: Int? = null,
    val bodyColor: Color = Color.Unspecified,
    val callToAction: NativeAdCallToActionStyle = NativeAdCallToActionStyle.Filled,
)
