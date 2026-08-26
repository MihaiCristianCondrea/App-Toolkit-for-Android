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

package com.mihaicristiancondrea.android.libs.apptoolkit.app.ads.data.repositories

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.FirebaseController
import com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore.CommonDataStore
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.DataState
import com.mihaicristiancondrea.android.libs.apptoolkit.core.domain.models.network.Errors
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

class TestDefaultAdsSettingsRepository {

    companion object {
        @JvmField
        @RegisterExtension
        val dispatcherExtension = UnconfinedDispatcherExtension()
    }

    private fun createRepository(dataStore: CommonDataStore): DefaultAdsSettingsRepository =
        DefaultAdsSettingsRepository(
            dataStore = dataStore,
            firebaseController = mockk<FirebaseController>(relaxed = true),
        )

    @Test
    fun `observeReduceAds emits datastore value`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.reduceAds } returns flowOf(true)
        val repository = createRepository(dataStore)

        repository.observeReduceAds().test {
            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeReduceAds propagates error`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.reduceAds } returns flow { throw IOException("boom") }
        val repository = createRepository(dataStore)

        repository.observeReduceAds().test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
        }
    }

    @Test
    fun `observeReduceAds rethrows cancellation`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.reduceAds } returns flow { throw CancellationException("boom") }
        val repository = createRepository(dataStore)

        val thrown = runCatching { repository.observeReduceAds().collect() }.exceptionOrNull()

        assertThat(thrown).isInstanceOf(CancellationException::class.java)
    }

    @Test
    fun `setReduceAds returns success when persisted`() =
        runTest(dispatcherExtension.testDispatcher) {
            val dataStore = mockk<CommonDataStore>()
            coEvery { dataStore.saveReduceAds(any()) } returns Unit
            val repository = createRepository(dataStore)

            val result = repository.setReduceAds(true)

            assertThat(result).isInstanceOf(DataState.Success::class.java)
            coVerify { dataStore.saveReduceAds(isChecked = true) }
        }

    // The failure has to be a value, not a throw: the settings screen renders it, and a raw throw
    // from a suspend call reaches the ViewModel's crash reporter instead of the snackbar.
    @Test
    fun `setReduceAds returns error on failure`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveReduceAds(any()) } throws IOException("boom")
        val repository = createRepository(dataStore)

        val result = repository.setReduceAds(true)

        assertThat(result).isInstanceOf(DataState.Error::class.java)
        assertThat((result as DataState.Error).error)
            .isEqualTo(Errors.Database.DATABASE_OPERATION_FAILED)
    }

    @Test
    fun `setReduceAds rethrows cancellation`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveReduceAds(any()) } throws CancellationException("cancelled")
        val repository = createRepository(dataStore)

        assertThrows<CancellationException> { repository.setReduceAds(true) }
    }
}
