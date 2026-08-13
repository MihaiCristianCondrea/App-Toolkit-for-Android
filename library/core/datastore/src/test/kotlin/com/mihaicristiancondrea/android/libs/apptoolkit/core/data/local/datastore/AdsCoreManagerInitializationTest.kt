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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.data.local.datastore

import android.content.Context
import android.util.Log
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.ads.AdsSdkInitializer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.AdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Regression cover for the crash where the Mobile Ads SDK was never initialized while the ad views
 * happily loaded ads: `NativeAdLoader.load` throws
 * `IllegalStateException("MobileAds.initialize must be called before using the Google Mobile Ads
 * SDK.")`, from inside composition, and the process dies.
 *
 * The cause was two different readings of the same preference — this manager sampled it once with a
 * `!isDebugBuild` default while the views read it with a `true` default — plus initialization that
 * was never retried when the preference changed.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AdsCoreManagerInitializationTest {

    private val adMobAppIdProvider = AdMobAppIdProvider { "ca-app-pub-1234567890123456~1234567890" }

    /** Counts initializations instead of touching the real SDK. */
    private class RecordingAdsSdkInitializer : AdsSdkInitializer {
        var initializations: Int = 0
            private set

        override fun initialize(context: Context, config: InitializationConfig) {
            initializations++
        }
    }

    private val initializer = RecordingAdsSdkInitializer()

    @BeforeEach
    fun mockAndroidLog() {
        mockkStatic(Log::class)
        every { Log.e(any<String>(), any<String>()) } returns 0
    }

    @AfterEach
    fun unmockAndroidLog() {
        unmockkStatic(Log::class)
        setCommonDataStoreSingleton(instance = null)
    }

    /**
     * [AdsCoreManager] resolves its data store through [CommonDataStore.getInstance] in its
     * constructor, which would build a real `DataStore` against a mock [Context]. Seeding the
     * singleton first keeps the manager on the stubbed store.
     */
    private fun setCommonDataStoreSingleton(instance: CommonDataStore?) {
        CommonDataStore::class.java.getDeclaredField("instance").apply {
            isAccessible = true
            set(null, instance)
        }
    }

    @Test
    fun `ads enablement is read from the same flow the ad views observe`() = runTest {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")

        verify { dataStore.adsEnabledFlow }
        verify(exactly = 0) { dataStore.ads(any()) }
    }

    @Test
    fun `the SDK is initialized when ads are enabled`() = runTest {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")

        assertEquals(1, initializer.initializations)
    }

    @Test
    fun `enabling ads at runtime initializes the SDK`() = runTest {
        val adsEnabled = MutableStateFlow(false)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns adsEnabled
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")
        assertEquals(0, initializer.initializations)

        adsEnabled.value = true

        assertEquals(1, initializer.initializations)
    }

    @Test
    fun `the SDK is initialized only once`() = runTest {
        val adsEnabled = MutableStateFlow(true)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns adsEnabled
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")
        adsEnabled.value = false
        adsEnabled.value = true

        assertEquals(1, initializer.initializations)
    }

    @Test
    fun `the SDK is not initialized when the host declares no AdMob app id`() = runTest {
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val manager = managerWith(
            dataStore = dataStore,
            adMobAppIdProvider = AdMobAppIdProvider { null },
        )

        manager.initializeAds(appOpenUnitId = "unit")

        assertEquals(0, initializer.initializations)
    }

    private fun managerWith(
        dataStore: CommonDataStore,
        adMobAppIdProvider: AdMobAppIdProvider = this.adMobAppIdProvider,
    ): AdsCoreManager {
        val context = mockk<Context>()
        every { context.applicationContext } returns context

        setCommonDataStoreSingleton(instance = dataStore)
        return AdsCoreManager(
            context,
            mockk<BuildInfoProvider>(),
            TestDispatchers(UnconfinedTestDispatcher()),
            adMobAppIdProvider,
            initializer,
        )
    }
}
