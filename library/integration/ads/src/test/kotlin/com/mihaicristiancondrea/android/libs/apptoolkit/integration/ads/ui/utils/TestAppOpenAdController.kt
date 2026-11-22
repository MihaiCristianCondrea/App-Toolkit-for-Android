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
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.exceptions.StorageException
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.local.AdMobAppIdDataSource
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.data.repositories.AdsSettingsRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import com.mihaicristiancondrea.android.libs.apptoolkit.integration.ads.data.remote.AdsSdkInitializer
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.time.TimeSource

@OptIn(ExperimentalCoroutinesApi::class)
class TestAppOpenAdController {
    private val testScope = CoroutineScope(Dispatchers.Unconfined)
    private val controllers = mutableListOf<AppOpenAdController>()

    /**
     * Eager dispatchers for tests using `runBlocking`. A queued [StandardTestDispatcher] cannot
     * progress there without scheduler advancement, leaving dispatcher switches suspended.
     */
    private fun eagerDispatchers(): TestDispatchers =
        TestDispatchers(UnconfinedTestDispatcher())

    /**
     * Counts SDK initialization calls without invoking the Android SDK.
     */
    private class RecordingAdsSdkInitializer : AdsSdkInitializer {
        var initializations: Int = 0
            private set

        override fun initialize(context: Context, config: InitializationConfig) {
            initializations++
        }
    }

    private lateinit var initializer: RecordingAdsSdkInitializer
    private lateinit var diagnostics: FakeTelemetryRepository

    @BeforeEach
    fun resetFixtures() {
        initializer = RecordingAdsSdkInitializer()
        diagnostics = FakeTelemetryRepository()
        // android.util.Log is a stub that throws in plain JVM unit tests, and
        // AppOpenAdController logs warnings and errors on missing IDs or SDK throws.
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.w(any(), any<String>(), any()) } returns 0
        every { Log.d(any(), any()) } returns 0
    }

    @AfterEach
    fun tearDown() {
        controllers.forEach(AppOpenAdController::close)
        controllers.clear()
        unmockkObject(AppOpenAd.Companion)
        unmockkStatic(Log::class)
    }

    private fun appContext(): Context = mockk<Context>(relaxed = true).also { context ->
        every { context.applicationContext } returns context
    }

    /**
     * Supply the mocked store during construction. Leaving the default creates a real DataStore
     * whose background reads can fail after setup and contaminate later tests.
     */
    private fun managerWith(
        context: Context = appContext(),
        adMobAppIdDataSource: AdMobAppIdDataSource = this.adMobAppIdDataSource,
        dataStore: AdsSettingsRepository = stubDataStore(adsEnabled = false),
    ): AppOpenAdController = AppOpenAdController(
        context = context,
        dispatchers = eagerDispatchers(),
        adMobAppIdDataSource = adMobAppIdDataSource,
        adsSdkInitializer = initializer,
        adsSettingsRepository = dataStore,
        diagnostics = diagnostics,
    ).also(controllers::add)

    private fun stubDataStore(
        adsEnabled: Boolean,
        reduceAds: Boolean = false,
    ): AdsSettingsRepository = mockk {
        every { this@mockk.adsEnabled } returns MutableStateFlow(adsEnabled)
        every { observeReduceAds() } returns flowOf(reduceAds)
    }

    /** Host-manifest AdMob ID fixture for manager initialization. */
    private val adMobAppIdDataSource = AdMobAppIdDataSource { "ca-app-pub-1234567890123456~1234567890" }

    @Test
    fun `initializeAds triggers MobileAds`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))

        runBlocking { manager.initializeAds("id") }

        assertEquals(1, initializer.initializations)
    }

    @Test
    fun `initializeAds skips MobileAds when the host declares no AdMob app id`() {
        val manager = managerWith(
            adMobAppIdDataSource = { null },
            dataStore = stubDataStore(adsEnabled = true),
        )

        runBlocking { manager.initializeAds("id") }

        assertEquals(0, initializer.initializations)
        val mgrField = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        assertNull(mgrField.get(manager))
        assertEquals(1, diagnostics.breadcrumbs.size)
    }

    @Test
    fun `showAdIfAvailable before init notifies completion listener`() {
        val manager = managerWith()
        var completed = false

        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = testScope,
            onShowAdCompleteListener = { completed = true },
        )

        assertTrue(completed)
    }

    @Test
    fun `loadAd does not load when already loading or available`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!

        val loadingField = inner.javaClass.getDeclaredField("isLoadingAd")
        loadingField.isAccessible = true
        loadingField.setBoolean(inner, true)

        mockkObject(AppOpenAd.Companion)
        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }
        verify(exactly = 0) { AppOpenAd.load(any(), any()) }

        loadingField.setBoolean(inner, false)
        val adField = inner.javaClass.getDeclaredField("appOpenAd")
        adField.isAccessible = true
        adField.set(inner, mockk<AppOpenAd>())
        val markField = inner.javaClass.getDeclaredField("loadMark")
        markField.isAccessible = true
        markField.set(inner, TimeSource.Monotonic.markNow())

        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }
        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `showAdIfAvailable loads when no ad`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        var completed = false
        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = testScope,
            onShowAdCompleteListener = { completed = true },
        )

        assertTrue(completed)
        verify { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `callback dismiss reloads ad`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        val ad = mockk<AppOpenAd>(relaxed = true)
        val mgrField = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!
        val adField = inner.javaClass.getDeclaredField("appOpenAd")
        adField.isAccessible = true
        adField.set(inner, ad)
        val loadMarkField = inner.javaClass.getDeclaredField("loadMark")
        loadMarkField.isAccessible = true
        loadMarkField.set(inner, TimeSource.Monotonic.markNow())

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        val slot = slot<AppOpenAdEventCallback>()
        every { ad.adEventCallback = capture(slot) } returns Unit

        var completed = false
        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = testScope,
            onShowAdCompleteListener = { completed = true },
        )

        slot.captured.onAdDismissedFullScreenContent()

        val showField = inner.javaClass.getDeclaredField("isShowingAd")
        showField.isAccessible = true
        assertFalse(showField.getBoolean(inner))
        assertTrue(completed)
        verify { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `ads disabled skips load and notifies completion listener`() {
        val adsEnabled = MutableStateFlow(true)
        val dataStore = mockk<AdsSettingsRepository> {
            every { this@mockk.adsEnabled } returns adsEnabled
            every { observeReduceAds() } returns flowOf(false)
        }
        val manager = managerWith(dataStore = dataStore)
        runBlocking { manager.initializeAds("unit") }
        adsEnabled.value = false

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        var completed = false
        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = testScope,
            onShowAdCompleteListener = { completed = true },
        )

        assertTrue(completed)
        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `reduce ads skips load and notifies completion listener`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true, reduceAds = true))
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        var completed = false
        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = testScope,
            onShowAdCompleteListener = { completed = true },
        )

        assertTrue(completed)
        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `reduce ads read failure fails closed and notifies completion listener`() {
        val dataStore = mockk<AdsSettingsRepository> {
            every { adsEnabled } returns MutableStateFlow(true)
            every { observeReduceAds() } returns flow {
                throw StorageException(StorageException.Reason.FAILED)
            }
        }
        val manager = managerWith(dataStore = dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        var completed = false
        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = testScope,
            onShowAdCompleteListener = { completed = true },
        )

        assertTrue(completed)
        assertEquals(1, diagnostics.nonFatals.size)
        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `show throwing fails closed, resets showing flag, notifies listener, and reloads`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        val ad = mockk<AppOpenAd>(relaxed = true)
        every { ad.show(any()) } throws IllegalStateException("window gone")

        val mgrField = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!
        val adField = inner.javaClass.getDeclaredField("appOpenAd")
        adField.isAccessible = true
        adField.set(inner, ad)
        val loadMarkField = inner.javaClass.getDeclaredField("loadMark")
        loadMarkField.isAccessible = true
        loadMarkField.set(inner, TimeSource.Monotonic.markNow())

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        var completed = false
        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = testScope,
            onShowAdCompleteListener = { completed = true },
        )

        val showField = inner.javaClass.getDeclaredField("isShowingAd")
        showField.isAccessible = true
        assertFalse(showField.getBoolean(inner))
        assertNull(adField.get(inner))
        assertTrue(completed)
        assertEquals(1, diagnostics.nonFatals.size)
        verify(exactly = 1) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `load failure resets loading flag`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!

        mockkObject(AppOpenAd.Companion)
        val slot = slot<AdLoadCallback<AppOpenAd>>()
        every {
            AppOpenAd.load(any(), capture(slot))
        } answers {
            slot.captured.onAdFailedToLoad(mockk())
        }

        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }

        val loadingField = inner.javaClass.getDeclaredField("isLoadingAd")
        loadingField.isAccessible = true
        assertFalse(loadingField.getBoolean(inner))
    }

    @Test
    fun `showAdIfAvailable ignores when already showing`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!
        val showingField = inner.javaClass.getDeclaredField("isShowingAd")
        showingField.isAccessible = true
        showingField.setBoolean(inner, true)

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        var completed = false
        manager.showAdIfAvailable(
            activity = mockk<Activity>(),
            scope = testScope,
            onShowAdCompleteListener = { completed = true },
        )

        assertFalse(completed)
        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `concurrent load requests chain correctly`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!

        mockkObject(AppOpenAd.Companion)
        val slot = slot<AdLoadCallback<AppOpenAd>>()
        every { AppOpenAd.load(any(), capture(slot)) } answers {}

        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }
        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }

        // The second request is suppressed while the first is still in flight.
        verify(exactly = 1) { AppOpenAd.load(any(), any()) }

        // Once the in-flight request settles without producing an ad, a new one is allowed through.
        slot.captured.onAdFailedToLoad(mockk())
        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }
        verify(exactly = 2) { AppOpenAd.load(any(), any()) }

        // A held, unexpired ad must suppress additional load requests.
        slot.captured.onAdLoaded(mockk())
        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }
        verify(exactly = 2) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `loadAd fails closed and resets loading flag when AppOpenAd throws`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AppOpenAdController::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!

        mockkObject(AppOpenAd.Companion)
        every { AppOpenAd.load(any(), any()) } throws RuntimeException("fail")

        val method = inner.javaClass.getDeclaredMethod("loadAd")
        method.isAccessible = true
        method.invoke(inner)

        val loadingField = inner.javaClass.getDeclaredField("isLoadingAd")
        loadingField.isAccessible = true
        assertFalse(loadingField.getBoolean(inner))
        assertEquals(1, diagnostics.nonFatals.size)
    }

    @Test
    fun `no app open ad is loaded while ads are disabled`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = false))
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        manager.showAdIfAvailable(mockk(), testScope)

        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
    }

    @Test
    fun `an app open ad is loaded while ads are enabled`() {
        val manager = managerWith(dataStore = stubDataStore(adsEnabled = true))
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        manager.showAdIfAvailable(mockk(), testScope)

        verify { AppOpenAd.load(any(), any()) }
    }
}
