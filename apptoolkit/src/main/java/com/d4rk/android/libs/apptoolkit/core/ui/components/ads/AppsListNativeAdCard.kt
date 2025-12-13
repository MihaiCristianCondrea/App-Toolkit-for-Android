package com.d4rk.android.libs.apptoolkit.core.ui.components.ads

import android.annotation.SuppressLint
import android.view.LayoutInflater
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.domain.model.ads.AdsConfig
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * A Composable that displays a native ad from Google AdMob within a card,
 * styled to fit into a grid or list of applications.
 *
 * This function handles the entire ad loading and display lifecycle. It observes
 * the user's ad preferences from [CommonDataStore] and will only request and show
 * an ad if ads are enabled and a valid ad unit ID is provided in the [adsConfig].
 *
 * The ad is loaded using an [AdLoader] and displayed in a [NativeAdView] which is
 * embedded in the composition using the [AndroidView] composable. The ad's visibility
 * is managed by the `isAdLoaded` state, ensuring the card is only composed when the ad
 * is successfully fetched.
 *
 * In preview mode (e.g., in Android Studio's Composable preview), a placeholder
 * [AppsListNativeAdPreview] is shown instead of a real ad.
 *
 * The [DisposableEffect] is used to properly destroy the [NativeAd] object when the
 * composable leaves the composition to prevent memory leaks.
 *
 * @param modifier The [Modifier] to be applied to the ad card.
 * @param adsConfig The configuration object containing ad-related settings,
 *                  including the `bannerAdUnitId` for the native ad. Note that the
 *                  parameter name is `bannerAdUnitId` for consistency with other ad
 *                  components, but it's used for a native ad here.
 */
@SuppressLint("InflateParams")
@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
fun AppsListNativeAdCard(
    modifier: Modifier = Modifier,
    adsConfig: AdsConfig
) { // FIXME: Unstable parameter 'adsConfig' prevents composable from being skippable
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val dataStore: CommonDataStore = remember { CommonDataStore.getInstance(context = context) }
    val showAds: Boolean by dataStore.adsEnabledFlow.collectAsStateWithLifecycle(initialValue = true)

    if (inspectionMode) {
        AppsListNativeAdPreview(modifier = modifier)
        return
    }

    if (!showAds || adsConfig.bannerAdUnitId.isBlank()) {
        return
    }

    val adRequest: AdRequest = remember { AdRequest.Builder().build() }

    var isAdLoaded by remember(adsConfig.bannerAdUnitId) { mutableStateOf(false) }
    val nativeAdView = remember {
        LayoutInflater.from(context)
            .inflate(R.layout.native_ad_apps_list_card, null) as NativeAdView
    }
    var currentNativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            currentNativeAd?.destroy()
            currentNativeAd = null
        }
    }

    LaunchedEffect(
        adsConfig.bannerAdUnitId,
        adRequest,
        showAds
    ) { // FIXME: Value of 'showAds' is always true
        if (!showAds || adsConfig.bannerAdUnitId.isBlank()) { // FIXME: Condition '!showAds' is always false
            isAdLoaded = false
            return@LaunchedEffect
        }

        isAdLoaded = false
        val adLoader: AdLoader = AdLoader.Builder(context, adsConfig.bannerAdUnitId)
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                bindAppsListNativeAd(adView = nativeAdView, nativeAd = nativeAd)
                nativeAdView.isVisible = true
                isAdLoaded = true
            }
            .withNativeAdOptions(
                NativeAdOptions.Builder()
                    .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_RIGHT)
                    .build()
            )
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    nativeAdView.isVisible = false
                    isAdLoaded = false
                }
            })
            .build()

        nativeAdView.isVisible = false
        adLoader.loadAd(adRequest)
    }

    if (isAdLoaded) {
        Card(
            modifier = modifier
                .fillMaxSize()
                .aspectRatio(1f),
            shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize)
        ) {
            AndroidView(
                // FIXME: Calling a UI Composable composable function where a androidx.compose.ui.UiComposable composable was expected
                modifier = Modifier.fillMaxSize(),
                factory = { nativeAdView },
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
