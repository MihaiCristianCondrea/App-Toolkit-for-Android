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

package com.mihaicristiancondrea.android.apps.apptoolkit.feature.tiles.ui.views.ads

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.LocalNativeAdViewFactory
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberNativeAdCache
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads.rememberNativeAdState

/** Load once per screen and expose a slot only after an ad is ready, leaving no empty spacing. */
@Composable
internal fun rememberOverviewNativeAd(
    placementKey: String,
    adUnitId: String,
    enabled: Boolean,
): (@Composable () -> Unit)? {
    val showAds = rememberAdsEnabled()
    val cache = rememberNativeAdCache()
    val state = rememberNativeAdState(
        adUnitId = adUnitId,
        enabled = enabled && showAds,
        slotName = placementKey,
        cache = cache,
        cacheKey = placementKey,
    )
    val factory = remember { QuickToolsNativeAdViewFactory() }
    if (!enabled || !showAds || state.ad == null) return null

    val content: @Composable () -> Unit = {
        CompositionLocalProvider(LocalNativeAdViewFactory provides factory) {
            QuickToolsNativeAdCard(
                adUnitId = adUnitId,
                initiallyLoaded = true,
                cache = cache,
                cacheKey = placementKey,
            )
        }
    }
    return content
}
