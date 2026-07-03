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

package com.d4rk.android.apps.apptoolkit.app.tiles.ui.components

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import com.d4rk.android.libs.apptoolkit.R
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.NativeAdViewHost
import com.d4rk.android.libs.apptoolkit.core.ui.views.ads.rememberAdsEnabled
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.GroupedItemPosition
import com.d4rk.android.libs.apptoolkit.core.ui.views.preferences.groupedCorners
import com.d4rk.android.libs.apptoolkit.core.utils.constants.ui.SizeConstants
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoader
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdRequest
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdView

/**
 * Loads and renders the native ad row used between Toolkit Tiles categories.
 *
 * [initiallyLoaded] keeps an already reported ad visible while this composable is moved from the
 * hidden preloader slot into the visible list, avoiding a transient status reset and layout gap.
 */
@SuppressLint("InflateParams")
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun QuickToolsNativeAdCard(
    modifier: Modifier = Modifier,
    adUnitId: String,
    position: GroupedItemPosition,
    initiallyLoaded: Boolean = false,
    onStatusChanged: (Boolean) -> Unit = {},
) {
    val inspectionMode = LocalInspectionMode.current
    val showAds: Boolean = rememberAdsEnabled()

    if (inspectionMode) {
        QuickToolsNativeAdPreview(modifier = modifier, position = position)
        return
    }

    val mainHandler: Handler = remember { Handler(Looper.getMainLooper()) }

    var isAdLoaded by remember(adUnitId) { mutableStateOf(initiallyLoaded) }
    var nativeAdView by remember { mutableStateOf<NativeAdView?>(null) }
    var currentNativeAd by remember { mutableStateOf<NativeAd?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            currentNativeAd?.destroy()
            currentNativeAd = null
        }
    }

    LaunchedEffect(adUnitId, showAds) {
        if (!showAds || adUnitId.isBlank()) {
            nativeAdView?.isVisible = false
            currentNativeAd?.destroy()
            currentNativeAd = null
            isAdLoaded = false
            onStatusChanged(false)
            return@LaunchedEffect
        }

        val adRequest: NativeAdRequest = NativeAdRequest.Builder(
            adUnitId,
            listOf(NativeAd.NativeAdType.NATIVE)
        ).build()

        nativeAdView?.isVisible = false
        NativeAdLoader.load(
            adRequest,
            object : NativeAdLoaderCallback {
                override fun onNativeAdLoaded(nativeAd: NativeAd) {
                    mainHandler.post {
                        currentNativeAd?.destroy()
                        currentNativeAd = nativeAd
                        nativeAdView?.let { view ->
                            bindQuickToolsNativeAd(adView = view, nativeAd = nativeAd)
                            view.isVisible = true
                        }
                        isAdLoaded = true
                        onStatusChanged(true)
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mainHandler.post {
                        nativeAdView?.isVisible = false
                        isAdLoaded = false
                        onStatusChanged(false)
                    }
                }
            }
        )
    }

    if (isAdLoaded) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .groupedCorners(
                    position = position,
                    outerRadius = SizeConstants.ExtraLargeIncreasedSize,
                ),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RectangleShape,
        ) {
            Box(modifier = Modifier.padding(SizeConstants.MediumSize)) {
                NativeAdViewHost(
                    modifier = Modifier.fillMaxWidth(),
                    layoutResId = R.layout.native_ad_quick_tools_row,
                    onNativeAdViewReady = { adView ->
                        if (nativeAdView !== adView) {
                            nativeAdView = adView
                        }
                    },
                    onUpdate = { view ->
                        view.isVisible = isAdLoaded
                        if (isAdLoaded) {
                            currentNativeAd?.let { nativeAd ->
                                bindQuickToolsNativeAd(adView = view, nativeAd = nativeAd)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun QuickToolsNativeAdPreview(
    modifier: Modifier,
    position: GroupedItemPosition,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .groupedCorners(
                position = position,
                outerRadius = SizeConstants.ExtraLargeIncreasedSize,
            ),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = RectangleShape,
    ) {
        Box(modifier = Modifier.padding(SizeConstants.MediumSize)) {
            Row(
                modifier = Modifier
                    .padding(SizeConstants.LargeSize)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            MaterialTheme.colorScheme.primaryContainer,
                            RoundedCornerShape(SizeConstants.MediumSize)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ad", style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.width(SizeConstants.LargeSize))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Native Ad Preview",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "This is how the ad row will look in the list.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun bindQuickToolsNativeAd(adView: NativeAdView, nativeAd: NativeAd) {
    val headlineView: TextView? = adView.findViewById(R.id.native_ad_headline)
    adView.headlineView = headlineView
    headlineView?.text = nativeAd.headline

    val bodyView: TextView? = adView.findViewById(R.id.native_ad_body)
    adView.bodyView = bodyView
    val bodyText: CharSequence? = nativeAd.body
    if (bodyText.isNullOrEmpty()) {
        bodyView?.isVisible = false
    } else {
        bodyView?.text = bodyText
        bodyView?.isVisible = true
    }

    val advertiserView: TextView? = adView.findViewById(R.id.native_ad_advertiser)
    adView.advertiserView = advertiserView
    val advertiserText: CharSequence? = nativeAd.advertiser
    if (advertiserText.isNullOrEmpty()) {
        advertiserView?.isVisible = false
    } else {
        advertiserView?.text = advertiserText
        advertiserView?.isVisible = true
    }

    val iconView: ImageView? = adView.findViewById(R.id.native_ad_icon)
    adView.iconView = iconView
    val icon = nativeAd.icon
    if (icon == null) {
        iconView?.isVisible = false
    } else {
        iconView?.setImageDrawable(icon.drawable)
        iconView?.isVisible = true
    }

    adView.registerNativeAd(nativeAd, null)
}
