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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.Dp
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.constants.ui.SizeConstants
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd

/**
 * The single entry point for rendering a native ad anywhere in the toolkit.
 *
 * Every native ad surface goes through this composable. New surfaces add a [NativeAdPresentation]
 * rather than a new component with its own view tree, so ad policy, lifecycle, palette handling, and
 * the disclosure label live in one place.
 *
 * Behaviour worth knowing:
 * - Nothing is rendered until an ad is actually bound. The container used to be drawn up front,
 *   which left an empty bordered card behind whenever the load failed.
 * - [onAdLoaded] reports `true` once an ad is bound and `false` when there is none, so hosts can
 *   collapse the slot together with its spacing instead of leaving a gap.
 * - The user's ads preference and a blank [adUnitId] both suppress the request entirely.
 * - Under [LocalInspectionMode] a [NativeAdPlaceholder] is drawn instead of loading anything.
 *
 * @param adUnitId the AdMob native ad unit to request.
 * @param presentation the shape the ad takes on screen.
 * @param position position inside a grouped list; affects the container's corners.
 * @param showContainer `false` renders the ad without a card behind it.
 * @param cornerRadius corner radius used when [position] is [GroupedItemPosition.SINGLE].
 * @param containerColor overrides the card container. The default is an unstyled card, so the ad
 * matches ordinary content; pass a colour on screens that build their cards differently, and in
 * consumer apps whose surfaces are their own. Ignored when [showContainer] is `false`.
 * @param onAdLoaded invoked with whether an ad is currently displayed.
 */
@Composable
fun NativeAdSlot(
    adUnitId: String,
    presentation: NativeAdPresentation,
    modifier: Modifier = Modifier,
    position: GroupedItemPosition = GroupedItemPosition.SINGLE,
    showContainer: Boolean = true,
    cornerRadius: Dp = SizeConstants.ExtraLargeSize,
    containerColor: Color = Color.Unspecified,
    onAdLoaded: (Boolean) -> Unit = {},
) {
    val currentOnAdLoaded: (Boolean) -> Unit by rememberUpdatedState(newValue = onAdLoaded)

    if (LocalInspectionMode.current) {
        NativeAdPlaceholder(
            presentation = presentation,
            modifier = modifier,
            position = position,
            showContainer = showContainer,
            cornerRadius = cornerRadius,
            containerColor = containerColor,
        )
        return
    }

    val adsEnabled: Boolean = rememberAdsEnabled()
    val nativeAd: NativeAd? = rememberNativeAd(adUnitId = adUnitId, enabled = adsEnabled)

    LaunchedEffect(nativeAd) {
        currentOnAdLoaded(nativeAd != null)
    }

    if (nativeAd == null) return

    NativeAdSurface(
        modifier = modifier,
        position = position,
        showContainer = showContainer,
        cornerRadius = cornerRadius,
        containerColor = containerColor,
    ) {
        NativeAdRenderer(
            presentation = presentation,
            nativeAd = nativeAd,
            palette = nativeAdPalette(),
        )
    }
}
