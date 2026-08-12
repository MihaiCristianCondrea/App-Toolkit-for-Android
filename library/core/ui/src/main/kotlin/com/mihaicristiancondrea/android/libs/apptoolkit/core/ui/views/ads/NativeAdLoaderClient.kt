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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticCompositionLocalOf
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoader
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdRequest

/**
 * The single seam between [rememberNativeAd] and the Google Mobile Ads SDK.
 *
 * Loading is the only part of the native ad stack that talks to the network, so isolating it behind
 * a one-method interface lets tests and screenshot tooling drive the renderer with a canned
 * [NativeAd] instead of a live request.
 */
@Stable
fun interface NativeAdLoaderClient {

    /** Starts a native ad request for [adUnitId] and reports the outcome to [callback]. */
    fun load(adUnitId: String, callback: NativeAdLoaderCallback)

    companion object {
        /** The production client, backed by [NativeAdLoader]. */
        val Default: NativeAdLoaderClient = NativeAdLoaderClient { adUnitId, callback ->
            val request: NativeAdRequest = NativeAdRequest.Builder(
                adUnitId,
                listOf(NativeAd.NativeAdType.NATIVE),
            ).build()
            NativeAdLoader.load(request, callback)
        }
    }
}

/**
 * The [NativeAdLoaderClient] used by every [NativeAdSlot] below this point in the composition.
 */
val LocalNativeAdLoaderClient = staticCompositionLocalOf { NativeAdLoaderClient.Default }
