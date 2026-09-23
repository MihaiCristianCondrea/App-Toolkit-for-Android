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

import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull
import kotlin.test.assertSame

class NativeAdCacheTest {

    private var now: Long = 0L
    private val requests: MutableList<NativeAdLoaderCallback> = mutableListOf()
    private val loader = NativeAdLoaderClient { _, callback -> requests += callback }
    private val reporter: AdLoadReporter = mockk(relaxed = true)
    private val cache = NativeAdCache(
        maxAgeMillis = MAX_AGE_MILLIS,
        clock = { now },
        postToMain = { block -> block() },
    )

    @Test
    fun `a slot composed again under its key gets its loaded ad back without a new request`() {
        val first = composeSlot(key = "ad_1")
        val ad: NativeAd = mockk(relaxed = true)
        requests.single().onNativeAdLoaded(ad)

        val second = composeSlot(key = "ad_1")

        assertSame(first, second)
        assertSame(ad, second.state.ad)
        assertEquals(1, requests.size)
    }

    @Test
    fun `a request still in flight is joined rather than repeated`() {
        val first = composeSlot(key = "ad_1")
        val second = composeSlot(key = "ad_1")

        assertSame(first, second)
        assertEquals(1, requests.size)
    }

    @Test
    fun `an ad that arrives after the cache is cleared is destroyed`() {
        val holder = composeSlot(key = "ad_1")
        cache.clear()
        val lateAd: NativeAd = mockk(relaxed = true)

        requests.single().onNativeAdLoaded(lateAd)

        verify(exactly = 1) { lateAd.destroy() }
        assertNull(holder.state.ad)
    }

    @Test
    fun `an ad past its age is replaced and destroyed`() {
        composeSlot(key = "ad_1")
        val oldAd: NativeAd = mockk(relaxed = true)
        requests.single().onNativeAdLoaded(oldAd)

        now += MAX_AGE_MILLIS
        val replacement = composeSlot(key = "ad_1")

        assertNull(replacement.state.ad)
        assertEquals(2, requests.size)
        verify(exactly = 1) { oldAd.destroy() }
    }

    @Test
    fun `a slot that came back empty asks again`() {
        val failed = composeSlot(key = "ad_1")
        requests.single().onAdFailedToLoad(noFill())

        val retried = composeSlot(key = "ad_1")

        assertNotSame(failed, retried)
        assertEquals(2, requests.size)
    }

    @Test
    fun `a different ad unit under the same key replaces the kept ad`() {
        composeSlot(key = "ad_1", adUnitId = "unit-a")
        val adForA: NativeAd = mockk(relaxed = true)
        requests.single().onNativeAdLoaded(adForA)

        composeSlot(key = "ad_1", adUnitId = "unit-b")

        verify(exactly = 1) { adForA.destroy() }
        assertEquals(2, requests.size)
    }

    @Test
    fun `clearing destroys every kept ad`() {
        val ads: List<NativeAd> = List(size = 3) { mockk(relaxed = true) }
        ads.forEachIndexed { index, ad ->
            composeSlot(key = "ad_$index")
            requests[index].onNativeAdLoaded(ad)
        }
        assertEquals(3, cache.retainedCount)

        cache.clear()

        ads.forEach { ad -> verify(exactly = 1) { ad.destroy() } }
        assertEquals(0, cache.retainedCount)
    }

    /** What `rememberNativeAdState` does for a cached slot: resolve, attach, then load. */
    private fun composeSlot(key: Any, adUnitId: String = AD_UNIT): NativeAdHolder {
        val holder = cache.holderFor(key = key, adUnitId = adUnitId)
        cache.attach(key = key, holder = holder)
        holder.load(loaderClient = loader, reporter = reporter, slotName = "test")
        return holder
    }

    private fun noFill(): LoadAdError = mockk(relaxed = true) {
        every { message } returns "No fill."
    }

    private companion object {
        const val AD_UNIT: String = "unit-a"
        const val MAX_AGE_MILLIS: Long = 60_000L
    }
}
