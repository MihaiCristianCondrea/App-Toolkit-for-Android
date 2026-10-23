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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.local

import app.cash.turbine.test
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakePreferencesDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AdsPreferencesDataSourceTest {

    @Test
    fun `close cancels internal coroutine scope`() = runTest {
        val dataStore = createDataStore(testScheduler)
        val scope = dataStore.extractScope()
        val job = scope.coroutineContext[Job]

        assertEquals(job?.isActive, true)

        dataStore.close()

        assertEquals(job?.isCancelled, true)
    }

    @Test
    fun `ads flow honors default and updates state`() = runTest {
        val dataStore = createDataStore(testScheduler)

        assertTrue(dataStore.ads(default = true).first())
        advanceUntilIdle()
        assertTrue(dataStore.adsEnabled.value)

        dataStore.saveAds(false)
        advanceUntilIdle()

        assertFalse(dataStore.ads(default = true).first())
        assertFalse(dataStore.adsEnabled.value)

        dataStore.close()
    }

    private fun createDataStore(scheduler: TestCoroutineScheduler): DefaultAdsPreferencesDataSource =
        DefaultAdsPreferencesDataSource(
            dataStore = FakePreferencesDataStore(),
            dispatchers = TestDispatchers(StandardTestDispatcher(scheduler)),
            defaultAdsEnabled = true,
        )

    private fun DefaultAdsPreferencesDataSource.extractScope(): CoroutineScope {
        val field = DefaultAdsPreferencesDataSource::class.java.getDeclaredField("scope")
        field.isAccessible = true
        return field.get(this) as CoroutineScope
    }
}
