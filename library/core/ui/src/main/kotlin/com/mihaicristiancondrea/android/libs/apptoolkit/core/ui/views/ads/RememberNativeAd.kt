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

import android.os.Handler
import android.os.Looper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.ads.AdsSdkState
import org.koin.compose.koinInject

/**
 * Loads a [NativeAd] for [adUnitId] and keeps it alive for as long as it is composed.
 *
 * Change rationale: the previous cards split ownership between a `DisposableEffect(Unit)` that only
 * destroyed on dispose and a `LaunchedEffect(view, adUnitId)` that could start another load. A
 * changed ad unit therefore leaked the previous [NativeAd]. A single `DisposableEffect(adUnitId, …)`
 * now owns both the load and the destroy, so re-keying destroys the old ad before requesting a new
 * one, and an ad that arrives after disposal is destroyed instead of retained.
 *
 * The request waits for [AdsSdkState.isReady]. Initialization of the Mobile Ads SDK is asynchronous
 * and starts during app startup, so a slot composed early would otherwise ask a SDK that is not up
 * yet, which throws, and then never retry. Keying the effect on readiness means the request starts
 * by itself the moment the SDK is up.
 *
 * @param adUnitId the ad unit to request; a blank value loads nothing.
 * @param enabled `false` releases any loaded ad and skips loading, e.g. when the user disabled ads.
 * @return the loaded ad, or `null` while loading, after a failure, or when disabled.
 */
@Composable
fun rememberNativeAd(adUnitId: String, enabled: Boolean = true): NativeAd? =
    rememberNativeAdState(adUnitId = adUnitId, enabled = enabled).ad

/**
 * What a slot has: an ad, or the reason it has none.
 *
 * [rememberNativeAd] returns only the ad, because that is all a caller needs to render one. A
 * caller that wants to say something about an empty slot, such as the debug placeholder, needs to
 * know whether the request came back without an ad or was never made at all, which is what this
 * carries.
 *
 * @property ad the loaded ad, or null while loading, after a failure, or when disabled.
 * @property failure why there is no ad, or null when one is loaded or still on its way.
 * @property detail the SDK's own description of the failure, when it gave one.
 */
@Immutable
data class NativeAdSlotState(
    val ad: NativeAd? = null,
    val failure: AdSlotFailure? = null,
    val detail: String? = null,
)

/**
 * [rememberNativeAd] with the reason an empty slot is empty.
 *
 * Failures are handed to [AdLoadReporter] on the way through, so every toolkit ad surface reports
 * the same way without each one remembering to.
 *
 * @param slotName how this placement is named in logs and Crashlytics. Defaults to the ad unit,
 * which is better than nothing but worth passing when the caller has a real name.
 */
@Composable
fun rememberNativeAdState(
    adUnitId: String,
    enabled: Boolean = true,
    slotName: String = adUnitId,
): NativeAdSlotState {
    val loaderClient: NativeAdLoaderClient = LocalNativeAdLoaderClient.current
    val reporter: AdLoadReporter = koinInject()
    val mainHandler: Handler = remember { Handler(Looper.getMainLooper()) }
    val isToolkitSdkReady: Boolean by AdsSdkState.isReady.collectAsStateWithLifecycle()
    // Named `loadedAd` rather than `nativeAd` so the `onNativeAdLoaded` override below can use the
    // parameter name its supertype declares without shadowing this state.
    var loadedAd: NativeAd? by remember { mutableStateOf(value = null) }
    var failure: AdSlotFailure? by remember { mutableStateOf(value = null) }
    var failureDetail: String? by remember { mutableStateOf(value = null) }

    DisposableEffect(adUnitId, enabled, isToolkitSdkReady, loaderClient) {
        loadedAd?.destroy()
        loadedAd = null
        failure = null
        failureDetail = null

        if (!enabled || adUnitId.isBlank()) {
            return@DisposableEffect onDispose { }
        }

        if (!AdsSdkState.canRequestAds()) {
            // Not reported: the SDK is still starting and this effect re-runs when it is ready.
            // Only a request that was attempted and could not be made is worth a non-fatal.
            failure = AdSlotFailure.NOT_REQUESTED
            failureDetail = "Waiting for the Mobile Ads SDK."
            return@DisposableEffect onDispose { }
        }

        var disposed = false
        // The loader throws synchronously when the Mobile Ads SDK has not been initialized, and this
        // effect runs during composition, an unhandled throw here takes the whole process down. An
        // ad slot must never do that: a slot that cannot load is a slot that renders nothing.
        runCatching {
            loaderClient.load(
                adUnitId,
                object : NativeAdLoaderCallback {
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        mainHandler.post {
                            if (disposed) {
                                nativeAd.destroy()
                                return@post
                            }
                            loadedAd?.destroy()
                            loadedAd = nativeAd
                        }
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        mainHandler.post {
                            if (disposed) return@post
                            loadedAd?.destroy()
                            loadedAd = null
                            failure = AdSlotFailure.NO_AD
                            failureDetail = "code=${adError.code} ${adError.message}"
                            reporter.onAdFailedToLoad(
                                slotName = slotName,
                                adUnitId = adUnitId,
                                errorCode = adError.code.toString(),
                                errorMessage = adError.message,
                            )
                        }
                    }
                },
            )
        }.onFailure { throwable ->
            failure = AdSlotFailure.NOT_REQUESTED
            failureDetail = throwable.message
            reporter.onAdRequestNotStarted(
                slotName = slotName,
                adUnitId = adUnitId,
                throwable = throwable,
            )
        }

        onDispose {
            disposed = true
            loadedAd?.destroy()
            loadedAd = null
        }
    }

    return NativeAdSlotState(ad = loadedAd, failure = failure, detail = failureDetail)
}
