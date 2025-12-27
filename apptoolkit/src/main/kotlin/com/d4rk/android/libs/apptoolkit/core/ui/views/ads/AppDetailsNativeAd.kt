package com.d4rk.android.libs.apptoolkit.core.ui.views.ads

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.android.material.button.MaterialButton

/**
 * A Composable function that displays a Google AdMob Native Ad, styled for an "app details" context.
 *
 * This function handles the entire lifecycle of loading and displaying a native ad. It uses
 * `AndroidView` to embed a traditional Android XML layout (`R.layout.native_ad_app_details`)
 * which is then populated with the ad's content.
 *
 * The ad will only be displayed if:
 * 1. The user has not disabled ads (checked via `CommonDataStore`).
 * 2. A valid ad unit ID is provided in the `adsConfig`.
 *
 * In Jetpack Compose preview mode (i.e., `LocalInspectionMode.current` is true), it shows
 * a placeholder `AppDetailsNativeAdPreview` instead of attempting to load a real ad.
 *
 * The function manages the ad's lifecycle using `DisposableEffect` to ensure the `NativeAd`
 * object is properly destroyed when the Composable leaves the composition, preventing memory leaks.
 *
 * @param modifier The modifier to be applied to the ad container.
 * @param adsConfig The configuration object containing ad-related settings, specifically the
 *   `bannerAdUnitId` which is used here for the native ad.
 */
@SuppressLint("InflateParams")
@Composable
fun AppDetailsNativeAd(
    modifier: Modifier = Modifier,
    adsConfig: AdsConfig
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val showAds: Boolean = rememberAdsEnabled()

    if (inspectionMode) {
        AppDetailsNativeAdPreview(modifier = modifier)
        return
    }

    if (!showAds || adsConfig.bannerAdUnitId.isBlank()) {
        return
    }

    val adRequest: AdRequest = remember { AdRequest.Builder().build() }

    var nativeAdView by remember { mutableStateOf<NativeAdView?>(null) }
    var currentNativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            currentNativeAd?.destroy()
            currentNativeAd = null
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            LayoutInflater.from(ctx)
                .inflate(R.layout.native_ad_app_details, null) as NativeAdView
        },
        update = { view ->
            if (nativeAdView !== view) {
                nativeAdView = view
            }
        }
    )

    LaunchedEffect(nativeAdView, adsConfig.bannerAdUnitId, adRequest) {
        val view: NativeAdView = nativeAdView ?: return@LaunchedEffect

        val adLoader: AdLoader = AdLoader.Builder(context, adsConfig.bannerAdUnitId)
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                bindAppDetailsNativeAd(adView = view, nativeAd = nativeAd)
                view.isVisible = true
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    view.isVisible = false
                }
            })
            .build()

        view.isVisible = false
        adLoader.loadAd(adRequest)
    }
}

@Composable
private fun AppDetailsNativeAdPreview(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Native Ad Preview",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun bindAppDetailsNativeAd(adView: NativeAdView, nativeAd: NativeAd) {
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

    val callToActionView: MaterialButton = adView.findViewById(R.id.native_ad_call_to_action)
    adView.callToActionView = callToActionView
    val callToActionText: CharSequence? = nativeAd.callToAction
    if (callToActionText.isNullOrEmpty()) {
        callToActionView.isVisible = false
    } else {
        callToActionView.text = callToActionText
        callToActionView.isVisible = true
    }

    adView.setNativeAd(nativeAd)
}
