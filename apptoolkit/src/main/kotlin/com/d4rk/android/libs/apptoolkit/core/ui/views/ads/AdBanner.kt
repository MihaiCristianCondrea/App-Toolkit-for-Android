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

package com.d4rk.android.libs.apptoolkit.core.ui.views.ads

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError

/**
 * A Composable function that displays a banner ad from Google AdMob.
 *
 * This component handles the entire lifecycle of an `AdView`. It observes whether ads are
 * enabled (via a `CommonDataStore`) and only attempts to load an ad if they are. The banner's
 * visibility is animated, expanding when an ad is loaded and shrinking when it's hidden or fails
 * to load.
 *
 * The ad view's lifecycle (resume, pause, destroy) is automatically managed in sync with the
 * Composable's lifecycle.
 *
 * @param modifier The [Modifier] to be applied to the ad container. The height is determined
 * by the `adsConfig.adSize`, but the width will fill the maximum available space.
 * @param adsConfig The [AdsConfig] object containing the necessary ad unit ID and ad size
 * for the banner ad.
 */
@Composable
fun AdBanner(
    modifier: Modifier = Modifier,
    adsConfig: AdsConfig
) {
    if (LocalInspectionMode.current) return

    val context: Context = LocalContext.current
    val showAds: Boolean = rememberAdsEnabled()

    var isAdLoaded by remember(adsConfig.bannerAdUnitId) { mutableStateOf(false) }

    val mainHandler: Handler = remember { Handler(Looper.getMainLooper()) }

    val adView = remember(adsConfig.bannerAdUnitId, adsConfig.adSize) {
        AdView(context)
    }

    LaunchedEffect(adView, showAds, adsConfig.bannerAdUnitId, adsConfig.adSize) {
        if (!showAds) {
            isAdLoaded = false
            return@LaunchedEffect
        }

        isAdLoaded = false
        val adRequest = BannerAdRequest.Builder(
            adsConfig.bannerAdUnitId,
            adsConfig.adSize
        ).build()
        adView.loadAd(
            adRequest,
            object : AdLoadCallback<BannerAd> {
                override fun onAdLoaded(ad: BannerAd) {
                    mainHandler.post { isAdLoaded = true }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mainHandler.post { isAdLoaded = false }
                }
            }
        )
    }

    AnimatedVisibility(
        visible = showAds && isAdLoaded,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(adsConfig.adSize.height.dp),
            factory = { adView }
        )
    }
}
