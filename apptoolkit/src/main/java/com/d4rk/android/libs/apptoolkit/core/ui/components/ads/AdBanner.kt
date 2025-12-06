package com.d4rk.android.libs.apptoolkit.core.ui.components.ads

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView

@Composable
fun AdBanner(modifier: Modifier = Modifier, adsConfig: AdsConfig) {
    if (LocalInspectionMode.current) return

    val context: Context = LocalContext.current
    val dataStore: CommonDataStore = remember { CommonDataStore.getInstance(context = context) }
    val showAds: Boolean by dataStore.adsEnabledFlow.collectAsStateWithLifecycle(initialValue = true)

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