package com.d4rk.android.libs.apptoolkit.data.core.ads

import android.app.Activity
import android.content.Context
import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.di.TestDispatchers
import com.d4rk.android.libs.apptoolkit.core.utils.interfaces.OnShowAdCompleteListener
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Test
import java.lang.reflect.InvocationTargetException
import java.util.Date
import kotlin.coroutines.Continuation
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.assertFailsWith

class TestAdsCoreManager {
    private val testScope = CoroutineScope(Dispatchers.Unconfined)
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
        val manager = AdsCoreManager(context, provider, TestDispatchers())

        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)

        mockkStatic(MobileAds::class)
        justRun { MobileAds.initialize(context) }

        runBlocking { manager.initializeAds("id") }
        verify { MobileAds.initialize(context) }
        println("🏁 [TEST DONE] initializeAds triggers MobileAds")
    }

    @Test
    fun `showAdIfAvailable before init does nothing`() {
        println("🚀 [TEST] showAdIfAvailable before init does nothing")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = AdsCoreManager(context, provider, TestDispatchers())
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
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(true)
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

        mockkStatic(AppOpenAd::class)
        inner.javaClass.getDeclaredMethod("loadAd", Context::class.java).apply {
            isAccessible = true
            invoke(inner, context)
        }
        verify(exactly = 0) { AppOpenAd.load(any(), any(), any(), any()) }

        loadingField.setBoolean(inner, false)
        val adField = inner.javaClass.getDeclaredField("appOpenAd")
        adField.isAccessible = true
        adField.set(inner, mockk<AppOpenAd>())
        val timeField = inner.javaClass.getDeclaredField("loadTime")
        timeField.isAccessible = true
        timeField.setLong(inner, Date().time)

        inner.javaClass.getDeclaredMethod("loadAd", Context::class.java).apply {
            isAccessible = true
            invoke(inner, context)
        }
        verify(exactly = 0) { AppOpenAd.load(any(), any(), any(), any()) }
        println("🏁 [TEST DONE] loadAd does not load when already loading or available")
    }

    @Test
    fun `showAdIfAvailable loads when no ad`() {
        println("🚀 [TEST] showAdIfAvailable loads when no ad")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkStatic(AppOpenAd::class)
        justRun { AppOpenAd.load(any(), any(), any(), any()) }

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
        verify { AppOpenAd.load(any(), any(), any(), any()) }
        println("🏁 [TEST DONE] showAdIfAvailable loads when no ad")
    }

    @Test
    fun `callback dismiss reloads ad`() {
        println("🚀 [TEST] callback dismiss reloads ad")
        val context = mockk<Context>(relaxed = true)
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(true)
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

        mockkStatic(AppOpenAd::class)
        justRun { AppOpenAd.load(any(), any(), any(), any()) }

        val slot = slot<FullScreenContentCallback>()
        every { ad.fullScreenContentCallback = capture(slot) } returns Unit

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
        verify { AppOpenAd.load(any(), any(), any(), any()) }
        println("🏁 [TEST DONE] callback dismiss reloads ad")
    }

    @Test
    fun `ads disabled skips load and show`() {
        println("🚀 [TEST] ads disabled skips load and show")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(false)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkStatic(AppOpenAd::class)
        justRun { AppOpenAd.load(any(), any(), any(), any()) }

        val activity = mockk<Activity>()
        manager.showAdIfAvailable(activity, testScope)

        verify(exactly = 0) { AppOpenAd.load(any(), any(), any(), any()) }
        println("🏁 [TEST DONE] ads disabled skips load and show")
    }

    @Test
    fun `load failure resets loading flag`() {
        println("🚀 [TEST] load failure resets loading flag")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!

        mockkStatic(AppOpenAd::class)
        val slot = slot<AppOpenAd.AppOpenAdLoadCallback>()
        every {
            AppOpenAd.load(any(), any(), any(), capture(slot))
        } answers {
            slot.captured.onAdFailedToLoad(mockk())
        }

        inner.javaClass.getDeclaredMethod("loadAd", Context::class.java).apply {
            isAccessible = true
            invoke(inner, context)
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
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(true)
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

        mockkStatic(AppOpenAd::class)
        justRun { AppOpenAd.load(any(), any(), any(), any()) }

        val method = inner.javaClass.getDeclaredMethod(
            "showAdIfAvailable",
            Activity::class.java,
            OnShowAdCompleteListener::class.java,
            Continuation::class.java
        )
        method.isAccessible = true
        method.invoke(inner, mockk<Activity>(), mockk<OnShowAdCompleteListener>(), noopContinuation)

        verify(exactly = 0) { AppOpenAd.load(any(), any(), any(), any()) }
        println("🏁 [TEST DONE] showAdIfAvailable ignores when already showing")
    }

    @Test
    fun `concurrent load requests chain correctly`() {
        println("🚀 [TEST] concurrent load requests chain correctly")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!

        mockkStatic(AppOpenAd::class)
        val slot = slot<AppOpenAd.AppOpenAdLoadCallback>()
        every { AppOpenAd.load(any(), any(), any(), capture(slot)) } answers {}

        inner.javaClass.getDeclaredMethod("loadAd", Context::class.java).apply {
            isAccessible = true
            invoke(inner, context)
        }
        inner.javaClass.getDeclaredMethod("loadAd", Context::class.java).apply {
            isAccessible = true
            invoke(inner, context)
        }

        verify(exactly = 1) { AppOpenAd.load(any(), any(), any(), any()) }

        slot.captured.onAdLoaded(mockk())

        inner.javaClass.getDeclaredMethod("loadAd", Context::class.java).apply {
            isAccessible = true
            invoke(inner, context)
        }

        verify(exactly = 2) { AppOpenAd.load(any(), any(), any(), any()) }
        println("🏁 [TEST DONE] concurrent load requests chain correctly")
    }

    @Test
    fun `loadAd propagates exceptions from AppOpenAd`() {
        println("🚀 [TEST] loadAd propagates exceptions from AppOpenAd")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.ads(any()) } returns flowOf(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        val mgrField = AdsCoreManager::class.java.getDeclaredField("appOpenAdManager")
        mgrField.isAccessible = true
        val inner = mgrField.get(manager)!!

        mockkStatic(AppOpenAd::class)
        every { AppOpenAd.load(any(), any(), any(), any()) } throws RuntimeException("fail")

        val method = inner.javaClass.getDeclaredMethod("loadAd", Context::class.java)
        method.isAccessible = true

        assertFailsWith<InvocationTargetException> { method.invoke(inner, context) }

        val loadingField = inner.javaClass.getDeclaredField("isLoadingAd")
        loadingField.isAccessible = true
        assert(loadingField.getBoolean(inner))
        println("🏁 [TEST DONE] loadAd propagates exceptions from AppOpenAd")
    }

    @Test
    fun `ads disabled by default when debug build`() {
        println("🚀 [TEST] ads disabled by default when debug build")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        every { provider.isDebugBuild } returns true
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        val slot = slot<Boolean>()
        every { dataStore.ads(capture(slot)) } returns flowOf(false)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkStatic(AppOpenAd::class)
        justRun { AppOpenAd.load(any(), any(), any(), any()) }

        manager.showAdIfAvailable(mockk(), testScope)

        assert(!slot.captured)
        verify(exactly = 0) { AppOpenAd.load(any(), any(), any(), any()) }
        println("🏁 [TEST DONE] ads disabled by default when debug build")
    }

    @Test
    fun `ads enabled by default when release build`() {
        println("🚀 [TEST] ads enabled by default when release build")
        val context = mockk<Context>()
        every { context.applicationContext } returns context
        val provider = mockk<BuildInfoProvider>()
        every { provider.isDebugBuild } returns false
        val manager = AdsCoreManager(context, provider, TestDispatchers())
        val dataStore = mockk<CommonDataStore>()
        val slot = slot<Boolean>()
        every { dataStore.ads(capture(slot)) } returns flowOf(true)
        val storeField = AdsCoreManager::class.java.getDeclaredField("dataStore")
        storeField.isAccessible = true
        storeField.set(manager, dataStore)
        runBlocking { manager.initializeAds("unit") }

        mockkStatic(AppOpenAd::class)
        justRun { AppOpenAd.load(any(), any(), any(), any()) }

        manager.showAdIfAvailable(mockk(), testScope)

        assert(slot.captured)
        verify { AppOpenAd.load(any(), any(), any(), any()) }
        println("🏁 [TEST DONE] ads enabled by default when release build")
    }

}
