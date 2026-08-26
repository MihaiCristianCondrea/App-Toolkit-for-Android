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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.ads

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * The one place debug and release builds behave differently, so both branches are pinned here.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class DefaultAdsDisplayPolicyTest {

    /**
     * Builds a policy over [limitAds] and cancels its sharing scope afterwards.
     *
     * The scope is unconfined so the shared state settles as soon as the preference changes, and
     * separate from the test scope, which would otherwise wait forever for a collector that by
     * design never completes.
     */
    private fun TestScope.withPolicy(
        limitAds: MutableStateFlow<Boolean>,
        isDebugBuild: Boolean,
        block: (AdsDisplayPolicy) -> Unit,
    ) {
        val scope = CoroutineScope(UnconfinedTestDispatcher(testScheduler))
        try {
            block(
                DefaultAdsDisplayPolicy(
                    limitAds = limitAds,
                    isDebugBuild = isDebugBuild,
                    scope = scope,
                )
            )
        } finally {
            scope.cancel()
        }
    }

    // Release is where the promise matters: opting in must never take the remaining ads away.
    @Test
    fun `a release build allows ads whatever the preference says`() = runTest {
        val limitAds = MutableStateFlow(false)
        withPolicy(limitAds, isDebugBuild = false) { policy ->
            assertThat(policy.adsAllowed.value).isTrue()

            limitAds.value = true

            assertThat(policy.adsAllowed.value).isTrue()
        }
    }

    @Test
    fun `a debug build stops ads when the preference is on`() = runTest {
        val limitAds = MutableStateFlow(false)
        withPolicy(limitAds, isDebugBuild = true) { policy ->
            assertThat(policy.adsAllowed.value).isTrue()

            limitAds.value = true

            assertThat(policy.adsAllowed.value).isFalse()

            limitAds.value = false

            assertThat(policy.adsAllowed.value).isTrue()
        }
    }

    @Test
    fun `a debug build with the preference already on starts with ads stopped`() = runTest {
        withPolicy(MutableStateFlow(true), isDebugBuild = true) { policy ->
            assertThat(policy.adsAllowed.value).isFalse()
        }
    }
}
