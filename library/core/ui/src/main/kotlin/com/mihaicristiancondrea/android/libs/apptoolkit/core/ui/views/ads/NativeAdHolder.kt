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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.android.libraries.ads.mobile.sdk.common.LoadAdError
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAd
import com.google.android.libraries.ads.mobile.sdk.nativead.NativeAdLoaderCallback

/**
 * One slot's ad and its request.
 *
 * It belongs either to a single composition, which releases it on disposal, or to a
 * [NativeAdCache], which keeps it across disposals. [state] is snapshot state, so a slot reading it
 * recomposes when the request completes, even if the request started under an earlier composition.
 *
 * Every request carries the generation it started in. [release] moves to the next generation, so a
 * response that arrives afterwards is destroyed instead of being shown by a slot that no longer
 * wants it.
 */
internal class NativeAdHolder(
    val adUnitId: String,
    private val clock: () -> Long,
    private val postToMain: (() -> Unit) -> Unit,
) {
    var state: NativeAdSlotState by mutableStateOf(value = NativeAdSlotState())
        private set

    private var isLoading: Boolean = false
    private var loadedAtMillis: Long = 0L
    private var generation: Int = 0

    /** Whether there is anything worth keeping: a loaded ad or a request still on its way. */
    val isRetained: Boolean
        get() = state.ad != null || isLoading

    fun isOlderThan(maxAgeMillis: Long): Boolean =
        state.ad != null && clock() - loadedAtMillis >= maxAgeMillis

    /** Marks the slot as waiting for the Mobile Ads SDK, unless it already has an ad to show. */
    fun waitForSdk() {
        if (isRetained) return
        state = NativeAdSlotState(
            failure = AdSlotFailure.NOT_REQUESTED,
            detail = "Waiting for the Mobile Ads SDK.",
        )
    }

    /**
     * Requests an ad unless one is already loaded or on its way.
     *
     * The loader throws synchronously when the Mobile Ads SDK has not been initialized, and this
     * runs from an effect, where an unhandled throw takes the whole process down. A slot that
     * cannot load renders nothing instead.
     */
    fun load(loaderClient: NativeAdLoaderClient, reporter: AdLoadReporter, slotName: String) {
        if (isRetained) return

        state = NativeAdSlotState()
        isLoading = true
        val requestGeneration: Int = generation
        runCatching {
            loaderClient.load(
                adUnitId,
                object : NativeAdLoaderCallback {
                    override fun onNativeAdLoaded(nativeAd: NativeAd) {
                        postToMain {
                            if (requestGeneration != generation) {
                                nativeAd.destroy()
                                return@postToMain
                            }
                            isLoading = false
                            state.ad?.destroy()
                            loadedAtMillis = clock()
                            state = NativeAdSlotState(ad = nativeAd)
                        }
                    }

                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        postToMain {
                            if (requestGeneration != generation) return@postToMain
                            isLoading = false
                            state = NativeAdSlotState(
                                failure = AdSlotFailure.NO_AD,
                                detail = "code=${adError.code} ${adError.message}",
                            )
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
            isLoading = false
            state = NativeAdSlotState(
                failure = AdSlotFailure.NOT_REQUESTED,
                detail = throwable.message,
            )
            reporter.onAdRequestNotStarted(
                slotName = slotName,
                adUnitId = adUnitId,
                throwable = throwable,
            )
        }
    }

    /** Destroys the ad, abandons any request, and leaves the holder ready to load again. */
    fun release() {
        generation++
        isLoading = false
        state.ad?.destroy()
        state = NativeAdSlotState()
    }
}
