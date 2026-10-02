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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.apps.ui.views.ads

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdCache
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdPresentation
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.NativeAdSlot

/**
 * Sample-owned square placement among app cards, using [NativeAdSlot] for rendering and
 * lifecycle.
 *
 * @param cache Retains an ad while its cell is off screen. Ignored without [cacheKey].
 * @param cacheKey Cell identity, normally its lazy-grid key.
 * @param containerColor Optional host surface override.
 * @param onAdLoaded Reports whether an ad is currently displayed.
 */
@Composable
fun AppsListNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    cache: NativeAdCache? = null,
    cacheKey: Any? = null,
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
        cache = cache,
        cacheKey = cacheKey,
        onAdLoaded = onAdLoaded,
    )
}
