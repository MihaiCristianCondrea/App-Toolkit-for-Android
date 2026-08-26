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

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.ads.mobile.sdk.banner.AdView
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAd
import com.google.android.libraries.ads.mobile.sdk.banner.BannerAdRequest
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.ads.AdsSdkState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.models.ads.AdsConfig

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

    var isAdLoaded by remember(adsConfig.bannerAdUnitId) { mutableStateOf(false) }

    val mainHandler: Handler = remember { Handler(Looper.getMainLooper()) }

    // Every entry point of the Mobile Ads SDK throws until MobileAds.initialize has run, and
    // initialization is asynchronous. Waiting on AdsSdkState keeps the banner from asking too early,
    // and re-keys this composable so the request starts as soon as the SDK is up.
    val isToolkitSdkReady: Boolean by AdsSdkState.isReady.collectAsStateWithLifecycle()

    val adView: AdView? = remember(adsConfig.bannerAdUnitId, adsConfig.adSize, isToolkitSdkReady) {
        if (!AdsSdkState.canRequestAds()) {
            null
        } else {
            runCatching { AdView(context) }
                .onFailure { throwable -> Log.w(LOG_TAG, "Could not create an AdView.", throwable) }
                .getOrNull()
        }
    }

    LaunchedEffect(adView, adsConfig.bannerAdUnitId, adsConfig.adSize) {
        if (adView == null) {
            isAdLoaded = false
            return@LaunchedEffect
        }

        isAdLoaded = false
        val adRequest = BannerAdRequest.Builder(
            adsConfig.bannerAdUnitId,
            adsConfig.adSize
        ).build()
        runCatching {
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
        }.onFailure { throwable ->
            Log.w(
                LOG_TAG,
                "Banner ad request for '${adsConfig.bannerAdUnitId}' could not be started.",
                throwable,
            )
            isAdLoaded = false
        }
    }

    AnimatedVisibility(
        visible = isAdLoaded && adView != null,
        enter = expandVertically(),
        exit = shrinkVertically()
    ) {
        AndroidView(
            modifier = modifier
                .fillMaxWidth()
                .height(adsConfig.adSize.height.dp),
            factory = { requireNotNull(adView) }
        )
    }
}

private const val LOG_TAG: String = "AdBanner"
