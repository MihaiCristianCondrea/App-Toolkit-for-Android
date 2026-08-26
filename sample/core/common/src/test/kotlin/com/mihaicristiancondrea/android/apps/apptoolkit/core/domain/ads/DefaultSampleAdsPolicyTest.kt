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

package com.mihaicristiancondrea.android.apps.apptoolkit.core.domain.ads

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultSampleAdsPolicyTest {

    /**
     * Builds a policy over [adsEnabled]/[reduceAds] and cancels its sharing scope afterwards.
     *
     * The scope is unconfined so the eagerly shared state settles as soon as a preference changes,
     * and separate from the test scope, which would otherwise wait forever for a collector that by
     * design never completes.
     */
    private fun TestScope.withPolicy(
        adsEnabled: MutableStateFlow<Boolean>,
        reduceAds: MutableStateFlow<Boolean>,
        block: (DefaultSampleAdsPolicy) -> Unit,
    ) {
        val scope = CoroutineScope(UnconfinedTestDispatcher(testScheduler))
        try {
            block(
                DefaultSampleAdsPolicy(
                    adsEnabled = adsEnabled,
                    reduceAdsPreference = reduceAds,
                    scope = scope,
                )
            )
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun `app open ads follow the normal policy`() = runTest {
        withPolicy(MutableStateFlow(true), MutableStateFlow(false)) { policy ->
            assertThat(policy.appOpenAdsEnabled.value).isTrue()
            assertThat(policy.reduceAds.value).isFalse()
        }
    }

    @Test
    fun `reducing ads suppresses app open ads`() = runTest {
        val reduceAds = MutableStateFlow(false)
        withPolicy(MutableStateFlow(true), reduceAds) { policy ->
            assertThat(policy.appOpenAdsEnabled.value).isTrue()

            reduceAds.value = true

            assertThat(policy.appOpenAdsEnabled.value).isFalse()
        }
    }

    // The legacy gate is upstream of everything: an install that opted out stays ad-free even
    // though it never opted into the reduced policy.
    @Test
    fun `grandfathered ad-free install gets no app open ads`() = runTest {
        withPolicy(MutableStateFlow(false), MutableStateFlow(false)) { policy ->
            assertThat(policy.appOpenAdsEnabled.value).isFalse()
            assertThat(policy.reduceAds.value).isFalse()
        }
    }

    @Test
    fun `native ad interval doubles under the reduced policy`() = runTest {
        val reduceAds = MutableStateFlow(false)
        withPolicy(MutableStateFlow(true), reduceAds) { policy ->
            NativeAdPlacement.entries.forEach { placement ->
                assertThat(policy.nativeAdInterval(placement)).isEqualTo(placement.normalInterval)
            }

            reduceAds.value = true

            NativeAdPlacement.entries.forEach { placement ->
                assertThat(policy.nativeAdInterval(placement))
                    .isEqualTo(placement.normalInterval * 2)
            }
        }
    }
}
