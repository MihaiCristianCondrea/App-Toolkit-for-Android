package com.d4rk.android.libs.apptoolkit.core.ui.components.ads

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.core.view.isVisible
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

@SuppressLint("InflateParams")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppsListNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val showAds: Boolean = rememberAdsEnabled()

    if (inspectionMode) {
        AppsListNativeAdPreview(modifier = modifier)
        return
    }

    val adRequest: AdRequest = remember { AdRequest.Builder().build() }

    var isAdLoaded by remember(adUnitId) { mutableStateOf(false) }
    var nativeAdView by remember { mutableStateOf<NativeAdView?>(null) }
    var currentNativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            currentNativeAd?.destroy()
            currentNativeAd = null
        }
    }

    LaunchedEffect(adUnitId, adRequest, showAds) {
        if (!showAds || adUnitId.isBlank()) {
            nativeAdView?.isVisible = false
            currentNativeAd?.destroy()
            currentNativeAd = null
            isAdLoaded = false
            return@LaunchedEffect
        }

        isAdLoaded = false
        val adLoader: AdLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                nativeAdView?.let { view ->
                    bindAppsListNativeAd(adView = view, nativeAd = nativeAd)
                    view.isVisible = true
                }
                isAdLoaded = true
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    nativeAdView?.isVisible = false
                    isAdLoaded = false
                }
            })
            .build()

        nativeAdView?.isVisible = false
        adLoader.loadAd(adRequest)
    }

    if (isAdLoaded) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(1f),
            shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize)
        ) {
            NativeAdViewHost(
                modifier = Modifier.fillMaxSize(),
                layoutResId = R.layout.native_ad_apps_list_card,
                onNativeAdViewReady = { adView ->
                    if (nativeAdView !== adView) {
                        nativeAdView = adView
                    }
                },
                onUpdate = { view ->
                    view.isVisible = isAdLoaded
                    if (isAdLoaded) {
                        currentNativeAd?.let { nativeAd ->
                            bindAppsListNativeAd(adView = view, nativeAd = nativeAd)
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun AppsListNativeAdPreview(modifier: Modifier) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .aspectRatio(1f),
        shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Native Ad Preview",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun bindAppsListNativeAd(adView: NativeAdView, nativeAd: NativeAd) {
    val headlineView: TextView = adView.findViewById(R.id.native_ad_headline)
    adView.headlineView = headlineView
    headlineView.text = nativeAd.headline

    val bodyView: TextView = adView.findViewById(R.id.native_ad_body)
    adView.bodyView = bodyView
    val bodyText: CharSequence? = nativeAd.body
    if (bodyText.isNullOrEmpty()) {
        bodyView.isVisible = false
    } else {
        bodyView.text = bodyText
        bodyView.isVisible = true
    }

    val advertiserView: TextView = adView.findViewById(R.id.native_ad_advertiser)
    adView.advertiserView = advertiserView
    val advertiserText: CharSequence? = nativeAd.advertiser
    if (advertiserText.isNullOrEmpty()) {
        advertiserView.isVisible = false
    } else {
        advertiserView.text = advertiserText
        advertiserView.isVisible = true
    }

    val iconView: ImageView = adView.findViewById(R.id.native_ad_icon)
    adView.iconView = iconView
    val icon = nativeAd.icon
    if (icon == null) {
        iconView.isVisible = false
    } else {
        iconView.setImageDrawable(icon.drawable)
        iconView.isVisible = true
    }

    adView.setNativeAd(nativeAd)
}
