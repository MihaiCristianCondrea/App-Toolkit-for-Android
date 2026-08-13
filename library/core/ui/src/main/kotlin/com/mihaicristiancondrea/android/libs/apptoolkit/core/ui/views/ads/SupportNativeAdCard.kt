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

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Media-led native ad card used on the Support screen.
 *
 * Change rationale: this used to inflate `R.layout.native_ad_support_card` and bind it with
 * `findViewById`. It is now a thin wrapper over [NativeAdSlot], which builds the same
 * `NativeAdView` in Kotlin. Three behaviours change with it:
 * - the card is no longer drawn before an ad exists, so a failed load leaves no empty bordered card;
 * - one `DisposableEffect(adUnitId)` owns both the load and the destroy, so changing the ad unit no
 *   longer leaks the previous `NativeAd`;
 * - the "Sponsored" disclosure comes from a translated string instead of an English literal.
 *
 * @param adUnitId AdMob native ad unit to request.
 * @param containerColor overrides the card container for hosts whose surfaces are their own.
 * @param onAdLoaded reports whether an ad is currently displayed, so hosts can collapse the slot.
 */
@Composable
fun SupportNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    containerColor: Color = Color.Unspecified,
    onAdLoaded: (Boolean) -> Unit = {},
) {
    NativeAdSlot(
        adUnitId = adUnitId,
        presentation = NativeAdPresentation.Featured,
        modifier = modifier,
        containerColor = containerColor,
        onAdLoaded = onAdLoaded,
    )
}
