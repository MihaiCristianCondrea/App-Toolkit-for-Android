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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.ads.AdsSdkState
import org.koin.compose.koinInject

/**
 * Loads a [NativeAd] for [adUnitId] and keeps it alive for as long as it is composed.
 *
 * One `DisposableEffect` owns both the request and the destroy, so re-keying destroys the old ad
 * before requesting a new one, and an ad that arrives after disposal is destroyed instead of
 * retained.
 *
 * The request waits for [AdsSdkState.isReady]. Initialization of the Mobile Ads SDK is asynchronous
 * and starts during app startup, so a slot composed early would otherwise ask a SDK that is not up
 * yet, which throws, and then never retry. Keying the effect on readiness means the request starts
 * by itself the moment the SDK is up.
 *
 * Inside a lazy list or grid, pass a [cache] and [cacheKey] so the ad outlives its item scrolling
 * out of view; see [NativeAdCache].
 *
 * @param adUnitId the ad unit to request; a blank value loads nothing.
 * @param enabled `false` releases any loaded ad and skips loading, e.g. when the user disabled ads.
 * @param cache keeps the ad while the slot is out of composition. Ignored without [cacheKey].
 * @param cacheKey identifies this slot in [cache]; unique among the slots composed at one time.
 * @return the loaded ad, or `null` while loading, after a failure, or when disabled.
 */
@Composable
fun rememberNativeAd(
    adUnitId: String,
    enabled: Boolean = true,
    cache: NativeAdCache? = null,
    cacheKey: Any? = null,
): NativeAd? = rememberNativeAdState(
    adUnitId = adUnitId,
    enabled = enabled,
    cache = cache,
    cacheKey = cacheKey,
).ad

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
 * @param cache keeps the ad while the slot is out of composition. Ignored without [cacheKey].
 * @param cacheKey identifies this slot in [cache]; unique among the slots composed at one time.
 */
@Composable
fun rememberNativeAdState(
    adUnitId: String,
    enabled: Boolean = true,
    slotName: String = adUnitId,
    cache: NativeAdCache? = null,
    cacheKey: Any? = null,
): NativeAdSlotState {
    val loaderClient: NativeAdLoaderClient = LocalNativeAdLoaderClient.current
    val reporter: AdLoadReporter = koinInject()
    val isToolkitSdkReady: Boolean by AdsSdkState.isReady.collectAsStateWithLifecycle()
    val sharedCache: NativeAdCache? = cache?.takeIf { cacheKey != null }

    val holder: NativeAdHolder = remember(sharedCache, cacheKey, adUnitId) {
        if (sharedCache != null && cacheKey != null) {
            sharedCache.holderFor(key = cacheKey, adUnitId = adUnitId)
        } else {
            NativeAdHolder(
                adUnitId = adUnitId,
                clock = System::currentTimeMillis,
                postToMain = mainThreadPoster(),
            )
        }
    }

    DisposableEffect(holder, enabled, isToolkitSdkReady, loaderClient) {
        if (sharedCache != null && cacheKey != null) {
            sharedCache.attach(key = cacheKey, holder = holder)
        }

        when {
            !enabled || adUnitId.isBlank() -> holder.release()
            // Not reported: the SDK is still starting and this effect re-runs when it is ready.
            // Only a request that was attempted and could not be made is worth a non-fatal.
            !AdsSdkState.canRequestAds() -> holder.waitForSdk()
            else -> holder.load(
                loaderClient = loaderClient,
                reporter = reporter,
                slotName = slotName,
            )
        }

        onDispose {
            // A cached holder stays with the cache, which destroys it when the screen leaves.
            if (sharedCache == null) holder.release()
        }
    }

    return holder.state
}
