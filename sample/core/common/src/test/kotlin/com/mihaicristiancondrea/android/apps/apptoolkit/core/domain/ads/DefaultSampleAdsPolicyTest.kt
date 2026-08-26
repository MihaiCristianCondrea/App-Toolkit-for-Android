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
        block: (SampleAdsPolicy) -> Unit,
    ) {
        val scope = CoroutineScope(UnconfinedTestDispatcher(testScheduler))
        try {
            block(
                DefaultSampleAdsPolicy(
                    adsEnabled = adsEnabled,
                    reduceAds = reduceAds,
                    scope = scope,
                )
            )
        } finally {
            scope.cancel()
        }
    }

    @Test
    fun `app open ads are shown under the normal policy`() = runTest {
        withPolicy(MutableStateFlow(true), MutableStateFlow(false)) { policy ->
            assertThat(policy.appOpenAdsEnabled.value).isTrue()
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

    @Test
    fun `ads disabled entirely means no app open ads`() = runTest {
        withPolicy(MutableStateFlow(false), MutableStateFlow(false)) { policy ->
            assertThat(policy.appOpenAdsEnabled.value).isFalse()
        }
    }
}
