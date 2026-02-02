package com.d4rk.android.libs.apptoolkit.core.ui.views.ads

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.isVisible
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.data.local.datastore.rememberCommonDataStore
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

/**
 * A Composable that displays a native ad from Google AdMob within a card.
 *
 * This component handles the entire lifecycle of loading and displaying a native ad. It uses
 * an `AndroidView` to inflate a traditional XML layout (`R.layout.native_ad_support_card`)
 * which serves as the `NativeAdView`.
 *
 * - It respects the user's ad preference stored in `CommonDataStore`. If ads are disabled,
 *   the component will not render anything.
 * - If the provided `adsConfig` has a blank `bannerAdUnitId`, it will also not render.
 * - In Jetpack Compose preview mode (i.e., in the IDE), it shows a `SupportNativeAdPreview`
 *   placeholder instead of a real ad.
 * - It manages the `NativeAd` object's lifecycle, ensuring it is destroyed when the
 *   composable leaves the composition to prevent memory leaks.
 * - The ad loading process is triggered via a `LaunchedEffect`. If the ad fails to load,
 *   the view's visibility is set to false. If successful, the ad content is bound to the
 *   `NativeAdView` using the `bindSupportNativeAd` helper function.
 *
 * @param modifier The modifier to be applied to the `OutlinedCard` that contains the ad.
 */
@SuppressLint("InflateParams")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SupportNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
) {
    val context = LocalContext.current
    val appContext = remember(context) { context.applicationContext }
    val inspectionMode = LocalInspectionMode.current

    val dataStore: CommonDataStore = rememberCommonDataStore()
    val showAds: Boolean by dataStore.adsEnabledFlow.collectAsStateWithLifecycle(initialValue = true)

    if (inspectionMode) return
    if (!showAds || adUnitId.isBlank()) return

    val adRequest: AdRequest = remember { AdRequest.Builder().build() }

    var nativeAdView by remember { mutableStateOf<NativeAdView?>(null) }
    var currentNativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            currentNativeAd?.destroy()
            currentNativeAd = null
        }
    }

    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize),
    ) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { ctx ->
                (LayoutInflater.from(ctx)
                    .inflate(R.layout.native_ad_support_card, null) as NativeAdView)
                    .also { it.isVisible = false }
            },
            update = { view ->
                if (nativeAdView !== view) nativeAdView = view
            }
        )
    }

    LaunchedEffect(nativeAdView, adUnitId) {
        val view: NativeAdView = nativeAdView ?: return@LaunchedEffect

        val adLoader: AdLoader = AdLoader.Builder(appContext, adUnitId)
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd

                bindSupportNativeAd(adView = view, nativeAd = nativeAd)
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

private fun bindSupportNativeAd(adView: NativeAdView, nativeAd: NativeAd) {
    val mediaView: MediaView = adView.findViewById(R.id.native_ad_media)
    adView.mediaView = mediaView
    val mediaContent = nativeAd.mediaContent
    if (mediaContent == null) {
        mediaView.isVisible = false
    } else {
        mediaView.mediaContent = mediaContent
        mediaView.isVisible = true
    }

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
