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
import android.os.SystemClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

/**
 * Keeps the native ads of one screen alive while their slots scroll in and out of view.
 *
 * A lazy list or grid disposes an item as soon as it leaves the viewport. An uncached slot destroys
 * its ad with it, so every scroll back sends a new request, shows an empty cell until it returns,
 * and lowers the match rate the network reports for the unit. A slot given this cache and a key
 * leaves its ad here on disposal and takes the same ad back the next time it is composed.
 *
 * Create one per screen with [rememberNativeAdCache], outside the lazy layout, and pass it with a
 * key to [NativeAdSlot], [rememberNativeAd], or [rememberNativeAdState]:
 *
 * ```kotlin
 * val adCache = rememberNativeAdCache()
 * LazyColumn {
 *     items(items, key = { it.key }) { item ->
 *         if (item is Row.Ad) {
 *             NativeAdSlot(
 *                 adUnitId = adUnitId,
 *                 presentation = NativeAdPresentation.Card,
 *                 cache = adCache,
 *                 cacheKey = item.key,
 *             )
 *         }
 *     }
 * }
 * ```
 *
 * The rules:
 * - A key must belong to one slot at a time. Two slots composed together under the same key would
 *   bind one ad to two views, which the SDK does not allow. The item's lazy-layout key fits well.
 * - A request still in flight when its slot scrolls away finishes into the cache.
 * - A slot that came back empty is not remembered, so it asks again the next time it is composed.
 * - An ad older than `maxAdAge` is replaced the next time its slot is composed, because the SDK
 *   asks for native ads to be shown within an hour of loading.
 * - Every ad is destroyed when the cache leaves composition. A configuration change therefore
 *   requests again; a `NativeAd` holds views and cannot outlive the activity.
 */
@Stable
class NativeAdCache internal constructor(
    private val maxAgeMillis: Long,
    private val clock: () -> Long = SystemClock::elapsedRealtime,
    private val postToMain: (() -> Unit) -> Unit = mainThreadPoster(),
) {
    private val holders: MutableMap<Any, NativeAdHolder> = HashMap()

    /** Slots whose ad or request this cache currently keeps. */
    internal val retainedCount: Int
        get() = holders.values.count(NativeAdHolder::isRetained)

    /**
     * The holder a slot composed under [key] should render from: the kept one when it still serves
     * [adUnitId], otherwise a new one, which only replaces the kept one once [attach] runs.
     *
     * Composition may be thrown away, so nothing is replaced or destroyed here.
     */
    internal fun holderFor(key: Any, adUnitId: String): NativeAdHolder {
        val kept: NativeAdHolder? = holders[key]
        val reusable: Boolean = kept != null &&
                kept.adUnitId == adUnitId &&
                kept.isRetained &&
                !kept.isOlderThan(maxAgeMillis)
        return if (reusable) kept else newHolder(adUnitId)
    }

    /** Records [holder] as the one behind [key], releasing whichever it replaces. */
    internal fun attach(key: Any, holder: NativeAdHolder) {
        val previous: NativeAdHolder? = holders.put(key, holder)
        if (previous != null && previous !== holder) previous.release()
    }

    /** Destroys every ad and cancels every request this cache keeps. */
    internal fun clear() {
        holders.values.forEach(NativeAdHolder::release)
        holders.clear()
    }

    internal fun newHolder(adUnitId: String): NativeAdHolder =
        NativeAdHolder(adUnitId = adUnitId, clock = clock, postToMain = postToMain)

    companion object {
        /** How long a loaded ad may wait to be shown before it is replaced. */
        val DefaultMaxAdAge: Duration = 1.hours
    }
}

/**
 * Remembers a [NativeAdCache] for the current screen and destroys its ads when the screen leaves.
 *
 * Call it outside the lazy layout whose slots use it; a cache remembered inside an item is disposed
 * with that item and keeps nothing.
 *
 * @param maxAdAge how long a loaded ad may be kept before its slot requests a fresh one.
 */
@Composable
fun rememberNativeAdCache(maxAdAge: Duration = NativeAdCache.DefaultMaxAdAge): NativeAdCache {
    val cache: NativeAdCache = remember(maxAdAge) {
        NativeAdCache(maxAgeMillis = maxAdAge.inWholeMilliseconds)
    }
    DisposableEffect(cache) {
        onDispose { cache.clear() }
    }
    return cache
}

/** Posts to the main thread, where the SDK's callbacks are moved before touching snapshot state. */
internal fun mainThreadPoster(): (() -> Unit) -> Unit {
    val handler = Handler(Looper.getMainLooper())
    return { block -> handler.post(block) }
}
