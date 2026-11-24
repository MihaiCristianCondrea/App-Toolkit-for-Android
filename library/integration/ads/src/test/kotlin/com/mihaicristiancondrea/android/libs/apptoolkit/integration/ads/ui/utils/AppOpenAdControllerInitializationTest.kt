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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.ui.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.AdMobAppIdDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.remote.AdsSdkInitializer
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * Verifies that manager initialization and ad-view enablement use the same preference,
 * including when ads become enabled after startup.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class AppOpenAdControllerInitializationTest {

    private val adMobAppIdDataSource = AdMobAppIdDataSource { "ca-app-pub-1234567890123456~1234567890" }
    private val controllers = mutableListOf<AppOpenAdController>()

    /** Counts initializations instead of touching the real SDK. */
    private class RecordingAdsSdkInitializer : AdsSdkInitializer {
        var initializations: Int = 0
            private set

        var lastConfig: InitializationConfig? = null
            private set

        override fun initialize(context: Context, config: InitializationConfig) {
            initializations++
            lastConfig = config
        }
    }

    private val initializer = RecordingAdsSdkInitializer()

    @BeforeEach
    fun mockAndroidLog() {
        mockkStatic(Log::class)
        every { Log.e(any<String>(), any<String>()) } returns 0
    }

    @AfterEach
    fun tearDown() {
        controllers.forEach(AppOpenAdController::close)
        controllers.clear()
        unmockkObject(AppOpenAd.Companion)
        unmockkStatic(Log::class)
    }

    // `ads(…)` returns a Flow, and inside a mockk `verify` block the call is recorded rather than
    // executed, nothing constructs a real flow here, so the UnusedFlow check does not apply.
    @Suppress("UnusedFlow")
    @Test
    fun `ads enablement is read from the same flow the ad views observe`() = runTest {
        val dataStore = mockk<AdsSettingsRepository>()
        every { dataStore.adsEnabled } returns MutableStateFlow(true)
        every { dataStore.observeReduceAds() } returns flowOf(false)
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")

        verify { dataStore.adsEnabled }
        verify(exactly = 0) { dataStore.observeAdsEnabled() }
    }

    @Test
    fun `the SDK is initialized when ads are enabled`() = runTest {
        val dataStore = mockk<AdsSettingsRepository>()
        every { dataStore.adsEnabled } returns MutableStateFlow(true)
        every { dataStore.observeReduceAds() } returns flowOf(false)
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")

        assertEquals(1, initializer.initializations)
    }

    @Test
    fun `enabling ads at runtime initializes the SDK`() = runTest {
        val adsEnabled = MutableStateFlow(false)
        val dataStore = mockk<AdsSettingsRepository>()
        every { dataStore.adsEnabled } returns adsEnabled
        every { dataStore.observeReduceAds() } returns flowOf(false)
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")
        assertEquals(0, initializer.initializations)

        adsEnabled.value = true

        assertEquals(1, initializer.initializations)
    }

    @Test
    fun `the SDK is initialized only once`() = runTest {
        val adsEnabled = MutableStateFlow(true)
        val dataStore = mockk<AdsSettingsRepository>()
        every { dataStore.adsEnabled } returns adsEnabled
        every { dataStore.observeReduceAds() } returns flowOf(false)
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")
        adsEnabled.value = false
        adsEnabled.value = true

        assertEquals(1, initializer.initializations)
    }

    @Test
    fun `the SDK is not initialized when the host declares no AdMob app id`() = runTest {
        val dataStore = mockk<AdsSettingsRepository>()
        every { dataStore.adsEnabled } returns MutableStateFlow(true)
        every { dataStore.observeReduceAds() } returns flowOf(false)
        val manager = managerWith(
            dataStore = dataStore,
            adMobAppIdDataSource = { null },
        )

        manager.initializeAds(appOpenUnitId = "unit")

        assertEquals(0, initializer.initializations)
    }

    @Test
    fun `a blank app open unit id initializes the SDK without registering an app open loader`() = runTest {
        val dataStore = mockk<AdsSettingsRepository>()
        every { dataStore.adsEnabled } returns MutableStateFlow(true)
        every { dataStore.observeReduceAds() } returns flowOf(false)
        val manager = managerWith(dataStore = dataStore)

        mockkObject(AppOpenAd.Companion)
        manager.initializeAds(appOpenUnitId = "   ")

        assertEquals(1, initializer.initializations)
        var completed = false
        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = backgroundScope,
            onShowAdCompleteListener = { completed = true },
        )
        assertTrue(completed)
        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `the native ad validator is disabled by default`() = runTest {
        val dataStore = mockk<AdsSettingsRepository>()
        every { dataStore.adsEnabled } returns MutableStateFlow(true)
        every { dataStore.observeReduceAds() } returns flowOf(false)
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit")

        assertTrue(initializer.lastConfig?.nativeAdDebuggerDisabled == true)
    }

    @Test
    fun `hosts can opt in to the native ad validator`() = runTest {
        val dataStore = mockk<AdsSettingsRepository>()
        every { dataStore.adsEnabled } returns MutableStateFlow(true)
        every { dataStore.observeReduceAds() } returns flowOf(false)
        val manager = managerWith(dataStore = dataStore)

        manager.initializeAds(appOpenUnitId = "unit", disableNativeValidator = false)

        assertFalse(initializer.lastConfig?.nativeAdDebuggerDisabled == true)
    }

    private fun managerWith(
        dataStore: AdsSettingsRepository,
        adMobAppIdDataSource: AdMobAppIdDataSource = this.adMobAppIdDataSource,
    ): AppOpenAdController {
        val context = mockk<Context>()
        every { context.applicationContext } returns context

        return AppOpenAdController(
            context = context,
            dispatchers = TestDispatchers(UnconfinedTestDispatcher()),
            adMobAppIdDataSource = adMobAppIdDataSource,
            adsSdkInitializer = initializer,
            adsSettingsRepository = dataStore,
        ).also(controllers::add)
    }
}
