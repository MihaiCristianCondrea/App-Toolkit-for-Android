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

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback

/**
 * Loads a [NativeAd] for [adUnitId] and keeps it alive for as long as it is composed.
 *
 * Change rationale: the previous cards split ownership between a `DisposableEffect(Unit)` that only
 * destroyed on dispose and a `LaunchedEffect(view, adUnitId)` that could start another load. A
 * changed ad unit therefore leaked the previous [NativeAd]. A single `DisposableEffect(adUnitId, …)`
 * now owns both the load and the destroy, so re-keying destroys the old ad before requesting a new
 * one, and an ad that arrives after disposal is destroyed instead of retained.
 *
 * @param adUnitId the ad unit to request; a blank value loads nothing.
 * @param enabled `false` releases any loaded ad and skips loading, e.g. when the user disabled ads.
 * @return the loaded ad, or `null` while loading, after a failure, or when disabled.
 */
@Composable
fun rememberNativeAd(adUnitId: String, enabled: Boolean = true): NativeAd? {
    val loaderClient: NativeAdLoaderClient = LocalNativeAdLoaderClient.current
    val mainHandler: Handler = remember { Handler(Looper.getMainLooper()) }
    var nativeAd: NativeAd? by remember { mutableStateOf(value = null) }

    DisposableEffect(adUnitId, enabled, loaderClient) {
        nativeAd?.destroy()
        nativeAd = null

        if (!enabled || adUnitId.isBlank()) {
            return@DisposableEffect onDispose { }
        }

        var disposed = false
        loaderClient.load(
            adUnitId,
            object : NativeAdLoaderCallback {
                override fun onNativeAdLoaded(ad: NativeAd) {
                    mainHandler.post {
                        if (disposed) {
                            ad.destroy()
                            return@post
                        }
                        nativeAd?.destroy()
                        nativeAd = ad
                    }
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mainHandler.post {
                        if (disposed) return@post
                        nativeAd?.destroy()
                        nativeAd = null
                    }
                }
            },
        )

        onDispose {
            disposed = true
            nativeAd?.destroy()
            nativeAd = null
        }
    }

    return nativeAd
}
