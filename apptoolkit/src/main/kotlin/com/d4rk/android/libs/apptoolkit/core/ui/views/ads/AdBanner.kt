package com.d4rk.android.libs.apptoolkit.core.ui.views.ads

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.d4rk.android.libs.apptoolkit.core.ui.model.ads.AdsConfig
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

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

    val adRequest = remember { AdRequest.Builder().build() }

    val adView = remember(adsConfig.bannerAdUnitId, adsConfig.adSize) {
        AdView(context).apply {
            this.adUnitId = adsConfig.bannerAdUnitId
            setAdSize(adsConfig.adSize)
        }
    }

    DisposableEffect(key1 = adView) {
        onDispose {
            adView.destroy()
        }
    }

    LaunchedEffect(key1 = adView, key2 = adRequest, key3 = showAds) {
        if (!showAds) {
            isAdLoaded = false
            return@LaunchedEffect
        }

        isAdLoaded = false
        adView.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                isAdLoaded = true
            }

            override fun onAdFailedToLoad(loadAdError: com.google.android.gms.ads.LoadAdError) {
                isAdLoaded = false
            }
        }
        adView.loadAd(adRequest)
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

        LifecycleResumeEffect(key1 = adView) {
            adView.resume()
            onPauseOrDispose {
                adView.pause()
            }
        }
    }
}
