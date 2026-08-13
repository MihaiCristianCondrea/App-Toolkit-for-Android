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

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAd
import com.google.android.libraries.ads.mobile.sdk.appopen.AppOpenAdEventCallback
import com.google.android.libraries.ads.mobile.sdk.common.AdLoadCallback
import com.google.android.libraries.ads.mobile.sdk.initialization.InitializationConfig
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.ads.AdsSdkInitializer
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.interfaces.OnShowAdCompleteListener
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.AdMobAppIdProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.utils.providers.BuildInfoProvider
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.TestDispatchers
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.lang.reflect.InvocationTargetException
import java.util.Date
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.assertFailsWith

class TestAdsCoreManager {
    private val testScope = CoroutineScope(Dispatchers.Unconfined)

    /**
     * Dispatchers for the `runBlocking` bodies below.
     *
     * These tests cannot use the default [TestDispatchers], which is backed by a
     * `StandardTestDispatcher`: that dispatcher only queues work and runs it when a
     * `TestCoroutineScheduler` is advanced, which nothing outside `runTest` does. `initializeAds`
     * opens with `withContext(dispatchers.io) { … }`, so under `runBlocking` that block was queued
     * and never executed, and every one of these tests parked forever — the whole
     * `testDebugUnitTest` task hung on this file rather than failing.
     *
     * `UnconfinedTestDispatcher` runs eagerly on the calling thread, so the `withContext` completes
     * inline and `runBlocking` returns.
     */
    private fun eagerDispatchers(): TestDispatchers =
        TestDispatchers(UnconfinedTestDispatcher())

    /**
     * Counts initializations instead of touching the real SDK.
     *
     * These tests used to stub the SDK with `mockkStatic(MobileAds::class)`, which never took
     * effect: `MobileAds.initialize` is declared on `MobileAds.Companion`, not as a static, so the
     * real implementation ran and threw `IllegalArgumentException: The provided application context
     * is not an instance of Application` against the mocked [Context]. [AdsCoreManager] takes an
     * [AdsSdkInitializer] precisely so a test never has to reach the SDK at all.
     */
    private class RecordingAdsSdkInitializer : AdsSdkInitializer {
        var initializations: Int = 0
            private set

        override fun initialize(context: Context, config: InitializationConfig) {
            initializations++
        }
    }

    private lateinit var initializer: RecordingAdsSdkInitializer

    @BeforeEach
    fun resetInitializer() {
        initializer = RecordingAdsSdkInitializer()
        // android.util.Log is a stub that throws in plain JVM unit tests, and
        // ensureAdsSdkInitialized logs when the host declares no AdMob app id.
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.d(any(), any()) } returns 0
    }

    private fun managerWith(
        context: Context,
        provider: BuildInfoProvider,
        adMobAppIdProvider: AdMobAppIdProvider = this.adMobAppIdProvider,
    ): AdsCoreManager = AdsCoreManager(
        context = context,
        buildInfoProvider = provider,
        dispatchers = eagerDispatchers(),
        adMobAppIdProvider = adMobAppIdProvider,
        adsSdkInitializer = initializer,
    )

    /**
     * [AdsCoreManager] now resolves the AdMob app id from the host manifest instead of using a
     * hardcoded sample id, so the tests have to supply one.
     */
    private val adMobAppIdProvider = AdMobAppIdProvider { "ca-app-pub-1234567890123456~1234567890" }
    private val noopContinuation = object : Continuation<Unit> {
        override val context = EmptyCoroutineContext
        override fun resumeWith(result: Result<Unit>) {
            result.getOrThrow()
        }
    }

    @Test
    fun `initializeAds triggers MobileAds`() {
        println("🚀 [TEST] initializeAds triggers MobileAds")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)

        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)

        runBlocking { manager.initializeAds("id") }

        assertEquals(1, initializer.initializations)
        println("🏁 [TEST DONE] initializeAds triggers MobileAds")
    }

    @Test
    fun `initializeAds skips MobileAds when the host declares no AdMob app id`() {
        println("🚀 [TEST] initializeAds skips MobileAds when the host declares no AdMob app id")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider, adMobAppIdProvider = { null })

        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)

        runBlocking { manager.initializeAds("id") }

        assertEquals(0, initializer.initializations)
        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        assertNull(mgrField.get(manager))
        println("🏁 [TEST DONE] initializeAds skips MobileAds when the host declares no AdMob app id")
    }

    @Test
    fun `showAdIfAvailable before init does nothing`() {
        println("🚀 [TEST] showAdIfAvailable before init does nothing")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val activity = mockk<Activity>()

        manager.showAdIfAvailable(activity, testScope)
        println("🏁 [TEST DONE] showAdIfAvailable before init does nothing")
    }

    @Test
    fun `loadAd does not load when already loading or available`() {
        println("🚀 [TEST] loadAd does not load when already loading or available")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
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
        val timeField = inner.javaClass.getDeclaredField("loadTime")
        timeField.isAccessible = true
        timeField.setLong(inner, Date().time)

        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }
        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
        println("🏁 [TEST DONE] loadAd does not load when already loading or available")
    }

    @Test
    fun `showAdIfAvailable loads when no ad`() {
        println("🚀 [TEST] showAdIfAvailable loads when no ad")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        var completed = false
        val mgrField2 = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
        mgrField2.isAccessible = true
        val inner2 = mgrField2.get(manager)!!
        val method = inner2.javaClass.getDeclaredMethod(
            "showAdIfAvailable",
            Activity::class.java,
            OnShowAdCompleteListener::class.java,
            Continuation::class.java
        )
        method.isAccessible = true
        val listener = object : OnShowAdCompleteListener {
            override fun onShowAdComplete() {
                completed = true
            }
        }
        method.invoke(inner2, mockk<Activity>(), listener, noopContinuation)

        assert(completed)
        verify { AppOpenAd.load(any(), any()) }
        println("🏁 [TEST DONE] showAdIfAvailable loads when no ad")
    }

    @Test
    fun `callback dismiss reloads ad`() {
        println("🚀 [TEST] callback dismiss reloads ad")
        val context = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val ad = mockk<AppOpenAd>(relaxed = true)
        val mgrField3 = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
        mgrField3.isAccessible = true
        val inner3 = mgrField3.get(manager)!!
        val adField = inner3.javaClass.getDeclaredField("appOpenAd")
        adField.isAccessible = true
        adField.set(inner3, ad)
        // isAdAvailable() is `appOpenAd != null && wasLoadTimeLessThanNHoursAgo()`. Planting the ad
        // without a load time leaves loadTime at 0, so the ad reads as expired, showAdIfAvailable
        // takes its reload branch, and adEventCallback is never assigned — which is what left the
        // capturing slot empty.
        val loadTimeField = inner3.javaClass.getDeclaredField("loadTime")
        loadTimeField.isAccessible = true
        loadTimeField.setLong(inner3, Date().time)

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        val slot = slot<AppOpenAdEventCallback>()
        every { ad.adEventCallback = capture(slot) } returns Unit

        val method2 = inner3.javaClass.getDeclaredMethod(
            "showAdIfAvailable",
            Activity::class.java,
            OnShowAdCompleteListener::class.java,
            Continuation::class.java
        )
        method2.isAccessible = true
        method2.invoke(inner3, mockk<Activity>(), object : OnShowAdCompleteListener {
            override fun onShowAdComplete() {}
        }, noopContinuation)

        slot.captured.onAdDismissedFullScreenContent()

        val showField = inner3.javaClass.getDeclaredField("isShowingAd")
        showField.isAccessible = true
        assertFalse(showField.getBoolean(inner3))
        verify { AppOpenAd.load(any(), any()) }
        println("🏁 [TEST DONE] callback dismiss reloads ad")
    }

    @Test
    fun `ads disabled skips load and show`() {
        println("🚀 [TEST] ads disabled skips load and show")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(false)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        val activity = mockk<Activity>()
        manager.showAdIfAvailable(activity, testScope)

        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
        println("🏁 [TEST DONE] ads disabled skips load and show")
    }

    @Test
    fun `load failure resets loading flag`() {
        println("🚀 [TEST] load failure resets loading flag")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
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
        println("🏁 [TEST DONE] load failure resets loading flag")
    }

    @Test
    fun `showAdIfAvailable ignores when already showing`() {
        println("🚀 [TEST] showAdIfAvailable ignores when already showing")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!
        val showingField = inner.javaClass.getDeclaredField("isShowingAd")
        showingField.isAccessible = true
        showingField.setBoolean(inner, true)

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        val method = inner.javaClass.getDeclaredMethod(
            "showAdIfAvailable",
            Activity::class.java,
            OnShowAdCompleteListener::class.java,
            Continuation::class.java
        )
        method.isAccessible = true
        method.invoke(inner, mockk<Activity>(), mockk<OnShowAdCompleteListener>(), noopContinuation)

        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
        println("🏁 [TEST DONE] showAdIfAvailable ignores when already showing")
    }

    @Test
    fun `concurrent load requests chain correctly`() {
        println("🚀 [TEST] concurrent load requests chain correctly")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
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

        // But once an ad is actually held, requesting again must not spend another impression:
        // loadAd()'s guard is `isLoadingAd || isAdAvailable()`. This assertion used to expect a
        // reload here, which the production code has never done.
        slot.captured.onAdLoaded(mockk())
        inner.javaClass.getDeclaredMethod("loadAd").apply {
            isAccessible = true
            invoke(inner)
        }
        verify(exactly = 2) { AppOpenAd.load(any(), any()) }
        println("🏁 [TEST DONE] concurrent load requests chain correctly")
    }

    @Test
    fun `loadAd propagates exceptions from AppOpenAd`() {
        println("🚀 [TEST] loadAd propagates exceptions from AppOpenAd")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!

        mockkObject(AppOpenAd.Companion)
        every { AppOpenAd.load(any(), any()) } throws RuntimeException("fail")

        val method = inner.javaClass.getDeclaredMethod("loadAd")
        method.isAccessible = true

        assertFailsWith<InvocationTargetException> { method.invoke(inner) }

        val loadingField = inner.javaClass.getDeclaredField("isLoadingAd")
        loadingField.isAccessible = true
        assert(loadingField.getBoolean(inner))
        println("🏁 [TEST DONE] loadAd propagates exceptions from AppOpenAd")
    }

    @Test
    fun `no app open ad is loaded while ads are disabled`() {
        println("🚀 [TEST] no app open ad is loaded while ads are disabled")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(false)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        manager.showAdIfAvailable(mockk(), testScope)

        verify(exactly = 0) { AppOpenAd.load(any(), any()) }
        println("🏁 [TEST DONE] no app open ad is loaded while ads are disabled")
    }

    @Test
    fun `an app open ad is loaded while ads are enabled`() {
        println("🚀 [TEST] an app open ad is loaded while ads are enabled")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = managerWith(context, provider)
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.adsEnabledFlow } returns MutableStateFlow(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkObject(AppOpenAd.Companion)
        justRun { AppOpenAd.load(any(), any()) }

        manager.showAdIfAvailable(mockk(), testScope)

        verify { AppOpenAd.load(any(), any()) }
        println("🏁 [TEST DONE] an app open ad is loaded while ads are enabled")
    }

}


