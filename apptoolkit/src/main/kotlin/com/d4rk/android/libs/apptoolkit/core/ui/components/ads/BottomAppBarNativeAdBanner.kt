package com.d4rk.android.libs.apptoolkit.core.ui.components.ads

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

private const val PREVIEW_NATIVE_AD_HEIGHT = 120

/**
 * A Composable that displays a native ad styled to fit in a bottom app bar.
 *
 * This component handles the entire lifecycle of loading and displaying a native ad from Google AdMob.
 * It uses an [AndroidView] to inflate a traditional XML layout (`R.layout.native_ad_bottom_bar`)
 * and populates it with the native ad content.
 *
 * The ad visibility is controlled by a value from [CommonDataStore], allowing users to disable ads.
 * If the ad fails to load, the view's visibility is set to false, effectively hiding it.
 * When the Composable is disposed, any active [NativeAd] is destroyed to prevent memory leaks.
 *
 * In Compose Preview mode (`LocalInspectionMode`), a placeholder [NativeAdPreview] is shown instead of a real ad.
 *
 * @param modifier The [Modifier] to be applied to the ad banner container.
 * @param adsConfig The configuration object containing ad-related settings, including the `bannerAdUnitId`.
 *                  The ad will not be displayed if the `bannerAdUnitId` is blank.
 */
@SuppressLint("InflateParams")
@Composable
fun BottomAppBarNativeAdBanner(
    modifier: Modifier = Modifier,
    adUnitId: String
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val dataStore: CommonDataStore = remember { CommonDataStore.getInstance(context = context) }
    val showAds: Boolean by dataStore.adsEnabledFlow.collectAsStateWithLifecycle(initialValue = true)

    if (inspectionMode) {
        NativeAdPreview(modifier = modifier)
        return
    }

    if (!showAds || adUnitId.isBlank()) {
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
        modifier = Modifier.fillMaxWidth(),
        factory = { ctx ->
            LayoutInflater.from(ctx)
                .inflate(R.layout.native_ad_bottom_bar, null) as NativeAdView
        },
        update = { view ->
            if (nativeAdView !== view) {
                nativeAdView = view
            }
        }
    )

    LaunchedEffect(nativeAdView, adUnitId, adRequest) {
        val view: NativeAdView = nativeAdView ?: return@LaunchedEffect

        val adLoader: AdLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                bindBottomAppBarNativeAd(adView = view, nativeAd = nativeAd)
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
private fun NativeAdPreview(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(PREVIEW_NATIVE_AD_HEIGHT.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Native Ad Preview",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private fun bindBottomAppBarNativeAd(adView: NativeAdView, nativeAd: NativeAd) {
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

    val callToActionView: Button = adView.findViewById(R.id.native_ad_call_to_action)
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
