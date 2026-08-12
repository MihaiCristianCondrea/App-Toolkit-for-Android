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

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Square native ad cell that sits among app cards in a grid.
 *
 * Change rationale: this used to inflate `R.layout.native_ad_apps_list_card` through
 * `NativeAdViewHost` and bind it with `findViewById`, while duplicating the "render nothing until
 * loaded" logic locally. Both now come from [NativeAdSlot] — see [SupportNativeAdCard] for the
 * behaviour changes that come with the shared renderer.
 *
 * @param containerColor overrides the card container for hosts whose surfaces are their own.
 * @param onAdLoaded reports whether an ad is currently displayed, so the grid can drop the cell.
 */
@Composable
fun AppsListNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    containerColor: Color = Color.Unspecified,
    onAdLoaded: (Boolean) -> Unit = {},
) {
    NativeAdSlot(
        adUnitId = adUnitId,
        presentation = NativeAdPresentation.Grid,
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(ratio = 1f),
        containerColor = containerColor,
        onAdLoaded = onAdLoaded,
    )
}
