package com.d4rk.android.libs.apptoolkit.app.ads.data

import app.cash.turbine.test
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.utils.dispatchers.UnconfinedDispatcherExtension
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.d4rk.android.libs.apptoolkit.core.domain.model.Result
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
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
        isDebugBuild: Boolean = false,
    ): DefaultAdsSettingsRepository {
        val buildInfoProvider = mockk<BuildInfoProvider> {
            every { isDebugBuild } returns isDebugBuild
        }
        return DefaultAdsSettingsRepository(
            dataStore = dataStore,
            buildInfoProvider = buildInfoProvider,
            ioDispatcher = dispatcherExtension.testDispatcher,
        )
    }

    @Test
    fun `observeAdsEnabled emits datastore value`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] observeAdsEnabled emits datastore value")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flowOf(false)
        val repository = createRepository(dataStore, isDebugBuild = false)

        repository.observeAdsEnabled().test {
            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeAdsEnabled emits default on error`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] observeAdsEnabled emits default on error")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flow { throw IOException("boom") }
        val repository = createRepository(dataStore, isDebugBuild = false)

        repository.observeAdsEnabled().test {
            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `observeAdsEnabled rethrows cancellation`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] observeAdsEnabled rethrows cancellation")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(default = true) } returns flow { throw CancellationException("boom") }
        val repository = createRepository(dataStore, isDebugBuild = false)

        var thrown: Throwable? = null
        try {
            repository.observeAdsEnabled().collect()
        } catch (e: Throwable) {
            thrown = e
        }

        assertThat(thrown).isInstanceOf(CancellationException::class.java)
    }

    @Test
    fun `setAdsEnabled returns success when persisted`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] setAdsEnabled returns success when persisted")
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveAds(any()) } returns Unit
        val repository = createRepository(dataStore, isDebugBuild = false)

        val result = repository.setAdsEnabled(true)

        assertThat(result).isInstanceOf(Result.Success::class.java)
        coVerify { dataStore.saveAds(isChecked = true) }
    }

    @Test
    fun `setAdsEnabled returns error on failure`() = runTest(dispatcherExtension.testDispatcher) {
        println("\uD83D\uDE80 [TEST] setAdsEnabled returns error on failure")
        val dataStore = mockk<CommonDataStore>()
        coEvery { dataStore.saveAds(any()) } throws IOException("boom")
        val repository = createRepository(dataStore, isDebugBuild = false)

        val result = repository.setAdsEnabled(true)

        assertThat(result).isInstanceOf(Result.Error::class.java)
        coVerify { dataStore.saveAds(isChecked = true) }
    }
}

