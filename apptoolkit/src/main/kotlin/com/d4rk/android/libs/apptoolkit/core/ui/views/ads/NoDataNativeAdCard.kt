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

import android.annotation.SuppressLint
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.surfaceColorAtElevation
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
import androidx.core.view.isVisible
import com.d4rk.android.libs.apptoolkit.R
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
 * A Composable that displays a native ad in a card format, specifically designed for "no data" or empty states.
 *
 * This component fetches and displays a native ad from Google AdMob. It handles the ad loading lifecycle,
 * including requesting, binding, and destroying the ad. It also respects the user's ad preference
 * stored in `CommonDataStore`.
 *
 * If the app is running in preview mode (via `LocalInspectionMode`), a placeholder UI is shown instead of a real ad.
 * The ad will not be displayed if the user has disabled ads or if the provided `adsConfig` does not contain a valid ad unit ID.
 *
 * The ad's visibility is managed internally; it's hidden during loading and on failure, and shown only upon successful ad retrieval.
 *
 * The layout for the native ad is inflated from the `R.layout.native_ad_no_data_card` XML file.
 *
 * @param modifier The modifier to be applied to the ad card.
 */
@SuppressLint("InflateParams")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun NoDataNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val showAds: Boolean = rememberAdsEnabled()

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

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(size = SizeConstants.ExtraLargeSize),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(SizeConstants.ExtraTinySize)
        )
    ) {
        NativeAdViewHost(
            modifier = Modifier.fillMaxWidth(),
            layoutResId = R.layout.native_ad_no_data_card,
            onNativeAdViewReady = { view ->
                if (nativeAdView !== view) {
                    nativeAdView = view
                }
            }
        )
    }

    LaunchedEffect(nativeAdView, adUnitId, adRequest) {
        val view: NativeAdView = nativeAdView ?: return@LaunchedEffect

        val adLoader: AdLoader = AdLoader.Builder(context, adUnitId)
            .forNativeAd { nativeAd ->
                currentNativeAd?.destroy()
                currentNativeAd = nativeAd
                bindNoDataNativeAd(adView = view, nativeAd = nativeAd)
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

private fun bindNoDataNativeAd(adView: NativeAdView, nativeAd: NativeAd) {
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
