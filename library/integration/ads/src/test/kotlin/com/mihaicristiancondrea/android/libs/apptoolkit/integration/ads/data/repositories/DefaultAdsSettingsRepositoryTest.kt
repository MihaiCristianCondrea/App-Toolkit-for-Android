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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.repositories

import app.cash.turbine.test
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.datastore.data.local.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.UnconfinedDispatcherExtension
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.RegisterExtension
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class DefaultAdsSettingsRepositoryTest {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private fun createRepository(
        dataStore: CommonDataStore,
        storeDefaultAdsEnabled: Boolean = true,
    ): DefaultAdsSettingsRepository {
        every { dataStore.defaultAdsEnabled } returns storeDefaultAdsEnabled
        return DefaultAdsSettingsRepository(
            dataStore = dataStore,
            telemetryRepository = FakeTelemetryRepository(),
        )
    }

    @Test
    fun `observeAdsEnabled emits the stored value`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flowOf(false)
        val repository = createRepository(dataStore)

        repository.observeAdsEnabled().test {
            assertFalse(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `observeAdsEnabled reads with the store's default`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = false) } returns flowOf(false)
        val repository = createRepository(dataStore, storeDefaultAdsEnabled = false)

        repository.observeAdsEnabled().test {
            assertFalse(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `observeAdsEnabled fails with a StorageException on a read error`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataStore = mockk<CommonDataStore>()
            every { dataStore.ads(default = true) } returns flow { throw IOException("boom") }
            val repository = createRepository(dataStore)

            repository.observeAdsEnabled().test {
                val error = assertIs<StorageException>(awaitError())
                assertEquals(StorageException.Reason.FAILED, error.reason)
            }
        }

    @Test
    fun `observeAdsEnabled passes other failures through`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flow { throw IllegalStateException("bug") }
        val repository = createRepository(dataStore)

        repository.observeAdsEnabled().test {
            assertIs<IllegalStateException>(awaitError())
        }
    }

    @Test
    fun `observeAdsEnabled rethrows cancellation`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flow { throw CancellationException("cancelled") }
        val repository = createRepository(dataStore)

        assertThrows<CancellationException> { repository.observeAdsEnabled().collect() }
    }

    @Test
    fun `observeReduceAds emits the stored value`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.reduceAds } returns flowOf(true)
        val repository = createRepository(dataStore)

        repository.observeReduceAds().test {
            assertTrue(awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `setAdsEnabled writes the preference`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveAds(any()) } returns Unit
        val repository = createRepository(dataStore)

        repository.setAdsEnabled(true)

        coVerify { dataStore.saveAds(isChecked = true) }
    }

    @Test
    fun `setAdsEnabled throws a StorageException when the write fails`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataStore = mockk<CommonDataStore>()
            coEvery { dataStore.saveAds(any()) } throws IOException("boom")
            val repository = createRepository(dataStore)

            val error = assertThrows<StorageException> { repository.setAdsEnabled(true) }

            assertEquals(StorageException.Reason.FAILED, error.reason)
        }

    @Test
    fun `setAdsEnabled rethrows cancellation`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveAds(any()) } throws CancellationException("cancelled")
        val repository = createRepository(dataStore)

        assertThrows<CancellationException> { repository.setAdsEnabled(true) }
    }

    @Test
    fun `setReduceAds writes the preference`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveReduceAds(any()) } returns Unit
        val repository = createRepository(dataStore)

        repository.setReduceAds(false)

        coVerify { dataStore.saveReduceAds(isChecked = false) }
    }

    @Test
    fun `setReduceAds throws a StorageException when the write fails`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataStore = mockk<CommonDataStore>()
            coEvery { dataStore.saveReduceAds(any()) } throws IOException("boom")
            val repository = createRepository(dataStore)

            assertThrows<StorageException> { repository.setReduceAds(true) }
        }
}
