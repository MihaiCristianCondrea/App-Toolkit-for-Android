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

    private fun createRepository(
        dataStore: CommonDataStore,
        storeDefaultAdsEnabled: Boolean = true,
    ): DefaultAdsSettingsRepository {
        every { dataStore.defaultAdsEnabled } returns storeDefaultAdsEnabled
        return DefaultAdsSettingsRepository(
            dataStore = dataStore,
            firebaseController = mockk<FirebaseController>(relaxed = true),
        )
    }

    @Test
    fun `observeAdsEnabled emits datastore value`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] observeAdsEnabled emits datastore value")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flowOf(false)
        val repository = createRepository(dataStore)

        repository.observeAdsEnabled().test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeAdsEnabled propagates error`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flow { throw IOException("boom") }
        val repository = createRepository(dataStore)

        repository.observeAdsEnabled().test {
            val error = awaitError()
            assertThat(error).isInstanceOf(IOException::class.java)
        }
    }

    @Test
    fun `observeAdsEnabled rethrows cancellation`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] observeAdsEnabled rethrows cancellation")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flow { throw CancellationException("boom") }
        val repository = createRepository(dataStore)

        val thrown = runCatching { repository.observeAdsEnabled().collect() }.exceptionOrNull()

        assertThat(thrown).isInstanceOf(CancellationException::class.java)
    }

    @Test
    fun `setAdsEnabled returns success when persisted`() =
        runTest(dispatcherExtension.testDispatcher) {
            println("\uD83D\uDE80 [TEST] setAdsEnabled returns success when persisted")
            val dataStore = mockk<CommonDataStore>()
            coEvery { dataStore.saveAds(any()) } returns Unit
            val repository = createRepository(dataStore)

            val result = repository.setAdsEnabled(true)

            assertThat(result).isInstanceOf(DataState.Success::class.java)
            coVerify { dataStore.saveAds(isChecked = true) }
        }

    @Test
    fun `setAdsEnabled returns error on failure`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] setAdsEnabled returns error on failure")
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveAds(any()) } throws IOException("boom")
        val repository = createRepository(dataStore)

        val result = repository.setAdsEnabled(true)

        assertThat(result).isInstanceOf(DataState.Error::class.java)
        assertThat((result as DataState.Error).error)
            .isEqualTo(Errors.Database.DATABASE_OPERATION_FAILED)
        coVerify { dataStore.saveAds(isChecked = true) }
    }

    // The failure has to be a value, not a throw: the settings screen renders it, and a raw throw
    // from a suspend call reaches the ViewModel's crash reporter instead of the snackbar.
    @Test
    fun `setAdsEnabled rethrows cancellation`() = runTest(dispatcherExtension.testDispatcher) {
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveAds(any()) } throws CancellationException("cancelled")
        val repository = createRepository(dataStore)

        assertThrows<CancellationException> { repository.setAdsEnabled(true) }
    }

    // The default belongs to the store, which `dataStoreModule` builds with ads on in every build.
    // These two only check that the repository reports what it was given rather than deciding for
    // itself; the pair used to be named for debug and release, which read as a policy this class
    // does not own.
    @Test
    fun `defaultAdsEnabled mirrors an enabled store`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = createRepository(dataStore = mockk(), storeDefaultAdsEnabled = true)
        assertThat(repository.defaultAdsEnabled).isTrue()
    }

    @Test
    fun `defaultAdsEnabled mirrors a disabled store`() = runTest(dispatcherExtension.testDispatcher) {
        val repository = createRepository(dataStore = mockk(), storeDefaultAdsEnabled = false)
        assertThat(repository.defaultAdsEnabled).isFalse()
    }
}

