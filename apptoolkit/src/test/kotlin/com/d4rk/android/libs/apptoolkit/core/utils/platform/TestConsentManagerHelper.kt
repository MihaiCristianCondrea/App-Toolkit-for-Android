package com.d4rk.android.libs.apptoolkit.core.utils.platform

import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.data.datastore.CommonDataStore
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TestConsentManagerHelper {

    @Test
    fun `updateConsent passes values to firebase`() {
        println("🚀 [TEST] updateConsent passes values to firebase")
        val analytics = mockk<FirebaseAnalytics>(relaxed = true)
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics
        justRun { analytics.setConsent(any()) }

        ConsentManagerHelper.updateConsent(
            analyticsGranted = true,
            adStorageGranted = false,
            adUserDataGranted = true,
            adPersonalizationGranted = false
        )

        verify {
            analytics.setConsent(match {
                it[FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE] == FirebaseAnalytics.ConsentStatus.GRANTED &&
                        it[FirebaseAnalytics.ConsentType.AD_STORAGE] == FirebaseAnalytics.ConsentStatus.DENIED &&
                        it[FirebaseAnalytics.ConsentType.AD_USER_DATA] == FirebaseAnalytics.ConsentStatus.GRANTED &&
                        it[FirebaseAnalytics.ConsentType.AD_PERSONALIZATION] == FirebaseAnalytics.ConsentStatus.DENIED
            })
        }
        println("🏁 [TEST DONE] updateConsent passes values to firebase")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `applyInitialConsent reads datastore and initializes firebase`() = runTest {
        println("🚀 [TEST] applyInitialConsent reads datastore and initializes firebase")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.analyticsConsent(any()) } returns flowOf(true)
        every { dataStore.adStorageConsent(any()) } returns flowOf(false)
        every { dataStore.adUserDataConsent(any()) } returns flowOf(true)
        every { dataStore.adPersonalizationConsent(any()) } returns flowOf(false)
        every { dataStore.usageAndDiagnostics(any()) } returns flowOf(true)

        val provider = mockk<BuildInfoProvider>()
        every { provider.isDebugBuild } returns false
        startKoin { modules(module { single<BuildInfoProvider> { provider } }) }

        val analytics = mockk<FirebaseAnalytics>(relaxed = true)
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics

        val crashlytics = mockk<FirebaseCrashlytics>(relaxed = true)
        val performance = mockk<FirebasePerformance>(relaxed = true)
        mockkStatic(FirebaseCrashlytics::class)
        mockkStatic(FirebasePerformance::class)
        every { FirebaseCrashlytics.getInstance() } returns crashlytics
        every { FirebasePerformance.getInstance() } returns performance

        ConsentManagerHelper.applyInitialConsent(dataStore)

        verify { analytics.setConsent(any()) }
        verify { analytics.setAnalyticsCollectionEnabled(true) }
        verify { crashlytics.isCrashlyticsCollectionEnabled = true }
        verify { performance.isPerformanceCollectionEnabled = true }

        stopKoin()
        println("🏁 [TEST DONE] applyInitialConsent reads datastore and initializes firebase")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateAnalyticsCollectionFromDatastore sets collection flags`() = runTest {
        println("🚀 [TEST] updateAnalyticsCollectionFromDatastore sets collection flags")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.usageAndDiagnostics(any()) } returnsMany listOf(
            flowOf(true),
            flowOf(false)
        )

        val analytics = mockk<FirebaseAnalytics>(relaxed = true)
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics
        val crashlytics = mockk<FirebaseCrashlytics>(relaxed = true)
        val performance = mockk<FirebasePerformance>(relaxed = true)
        mockkStatic(FirebaseCrashlytics::class)
        mockkStatic(FirebasePerformance::class)
        every { FirebaseCrashlytics.getInstance() } returns crashlytics
        every { FirebasePerformance.getInstance() } returns performance

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        verify { analytics.setAnalyticsCollectionEnabled(true) }
        verify { crashlytics.isCrashlyticsCollectionEnabled = true }
        verify { performance.isPerformanceCollectionEnabled = true }

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        verify { analytics.setAnalyticsCollectionEnabled(false) }
        verify { crashlytics.isCrashlyticsCollectionEnabled = false }
        verify { performance.isPerformanceCollectionEnabled = false }
        println("🏁 [TEST DONE] updateAnalyticsCollectionFromDatastore sets collection flags")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `applyInitialConsent propagates io exception`() = runTest {
        println("🚀 [TEST] applyInitialConsent propagates io exception")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.analyticsConsent(any()) } returns flow { throw java.io.IOException("io") }
        every { dataStore.adStorageConsent(any()) } returns flowOf(true)
        every { dataStore.adUserDataConsent(any()) } returns flowOf(true)
        every { dataStore.adPersonalizationConsent(any()) } returns flowOf(true)

        val provider = mockk<BuildInfoProvider>()
        every { provider.isDebugBuild } returns false
        startKoin { modules(module { single<BuildInfoProvider> { provider } }) }

        val field =
            ConsentManagerHelper::class.java.getDeclaredField($$"defaultAnalyticsGranted$delegate")
        field.isAccessible = true
        field.set(ConsentManagerHelper, lazy { !provider.isDebugBuild })

        assertFailsWith<java.io.IOException> {
            ConsentManagerHelper.applyInitialConsent(dataStore)
        }

        stopKoin()
        println("🏁 [TEST DONE] applyInitialConsent propagates io exception")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `applyInitialConsent propagates cancellation exception`() = runTest {
        println("🚀 [TEST] applyInitialConsent propagates cancellation exception")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.analyticsConsent(any()) } returns flow {
            throw kotlinx.coroutines.CancellationException(
                "cancel"
            )
        }
        every { dataStore.adStorageConsent(any()) } returns flowOf(true)
        every { dataStore.adUserDataConsent(any()) } returns flowOf(true)
        every { dataStore.adPersonalizationConsent(any()) } returns flowOf(true)

        val provider = mockk<BuildInfoProvider>()
        every { provider.isDebugBuild } returns false
        startKoin { modules(module { single<BuildInfoProvider> { provider } }) }

        val field =
            ConsentManagerHelper::class.java.getDeclaredField($$"defaultAnalyticsGranted$delegate")
        field.isAccessible = true
        field.set(ConsentManagerHelper, lazy { !provider.isDebugBuild })

        assertFailsWith<kotlinx.coroutines.CancellationException> {
            ConsentManagerHelper.applyInitialConsent(dataStore)
        }

        stopKoin()
        println("🏁 [TEST DONE] applyInitialConsent propagates cancellation exception")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateAnalyticsCollectionFromDatastore propagates firebase failure`() = runTest {
        println("🚀 [TEST] updateAnalyticsCollectionFromDatastore propagates firebase failure")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.usageAndDiagnostics(any()) } returns flowOf(true)

        val analytics = mockk<FirebaseAnalytics>(relaxed = true)
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics
        val crashlytics = mockk<FirebaseCrashlytics>()
        val performance = mockk<FirebasePerformance>(relaxed = true)
        mockkStatic(FirebaseCrashlytics::class)
        mockkStatic(FirebasePerformance::class)
        every { FirebaseCrashlytics.getInstance() } returns crashlytics
        every { FirebasePerformance.getInstance() } returns performance
        every { crashlytics.isCrashlyticsCollectionEnabled = any() } throws RuntimeException("fail")

        assertFailsWith<RuntimeException> {
            ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        }
        println("🏁 [TEST DONE] updateAnalyticsCollectionFromDatastore propagates firebase failure")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `applyInitialConsent when debug build uses datastore values`() = runTest {
        println("🚀 [TEST] applyInitialConsent when debug build uses datastore values")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.analyticsConsent(any()) } returns flowOf(false)
        every { dataStore.adStorageConsent(any()) } returns flowOf(false)
        every { dataStore.adUserDataConsent(any()) } returns flowOf(false)
        every { dataStore.adPersonalizationConsent(any()) } returns flowOf(false)
        every { dataStore.usageAndDiagnostics(any()) } returns flowOf(false)

        val provider = mockk<BuildInfoProvider>()
        every { provider.isDebugBuild } returns true
        startKoin { modules(module { single<BuildInfoProvider> { provider } }) }

        val field =
            ConsentManagerHelper::class.java.getDeclaredField($$"defaultAnalyticsGranted$delegate")
        field.isAccessible = true
        field.set(ConsentManagerHelper, lazy { !provider.isDebugBuild })

        val analytics = mockk<FirebaseAnalytics>(relaxed = true)
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics
        val crashlytics = mockk<FirebaseCrashlytics>(relaxed = true)
        val performance = mockk<FirebasePerformance>(relaxed = true)
        mockkStatic(FirebaseCrashlytics::class)
        mockkStatic(FirebasePerformance::class)
        every { FirebaseCrashlytics.getInstance() } returns crashlytics
        every { FirebasePerformance.getInstance() } returns performance

        ConsentManagerHelper.applyInitialConsent(dataStore)

        verify { analytics.setConsent(any()) }
        verify { analytics.setAnalyticsCollectionEnabled(false) }
        verify { crashlytics.isCrashlyticsCollectionEnabled = false }
        verify { performance.isPerformanceCollectionEnabled = false }

        stopKoin()
        println("🏁 [TEST DONE] applyInitialConsent when debug build uses datastore values")
    }

    @Test
    fun `updateConsent propagates firebase exception`() {
        println("🚀 [TEST] updateConsent propagates firebase exception")
        val analytics = mockk<FirebaseAnalytics>()
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics
        every { analytics.setConsent(any()) } throws RuntimeException("fail")

        assertFailsWith<RuntimeException> {
            ConsentManagerHelper.updateConsent(
                analyticsGranted = true,
                adStorageGranted = true,
                adUserDataGranted = true,
                adPersonalizationGranted = true
            )
        }
        println("🏁 [TEST DONE] updateConsent propagates firebase exception")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateAnalyticsCollectionFromDatastore toggles false to true`() = runTest {
        println("🚀 [TEST] updateAnalyticsCollectionFromDatastore toggles false to true")
        val dataStore = mockk<CommonDataStore>()
        every { dataStore.usageAndDiagnostics(any()) } returnsMany listOf(
            flowOf(false),
            flowOf(true)
        )

        val analytics = mockk<FirebaseAnalytics>(relaxed = true)
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics
        val crashlytics = mockk<FirebaseCrashlytics>(relaxed = true)
        val performance = mockk<FirebasePerformance>(relaxed = true)
        mockkStatic(FirebaseCrashlytics::class)
        mockkStatic(FirebasePerformance::class)
        every { FirebaseCrashlytics.getInstance() } returns crashlytics
        every { FirebasePerformance.getInstance() } returns performance

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        verify { analytics.setAnalyticsCollectionEnabled(false) }
        verify { crashlytics.isCrashlyticsCollectionEnabled = false }
        verify { performance.isPerformanceCollectionEnabled = false }

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        verify { analytics.setAnalyticsCollectionEnabled(true) }
        verify { crashlytics.isCrashlyticsCollectionEnabled = true }
        verify { performance.isPerformanceCollectionEnabled = true }
        println("🏁 [TEST DONE] updateAnalyticsCollectionFromDatastore toggles false to true")
    }

    @Test
    fun `defaultAnalyticsGranted matches inverse of debug mode`() {
        println("🚀 [TEST] defaultAnalyticsGranted matches inverse of debug mode")
        val provider = mockk<BuildInfoProvider>()
        every { provider.isDebugBuild } returnsMany listOf(true, false)
        startKoin { modules(module { single<BuildInfoProvider> { provider } }) }

        val field =
            ConsentManagerHelper::class.java.getDeclaredField($$"defaultAnalyticsGranted$delegate")
        field.isAccessible = true

        field.set(ConsentManagerHelper, lazy { !provider.isDebugBuild })
        assertFalse(ConsentManagerHelper.defaultAnalyticsGranted)

        field.set(ConsentManagerHelper, lazy { !provider.isDebugBuild })
        assertTrue(ConsentManagerHelper.defaultAnalyticsGranted)

        stopKoin()
        println("🏁 [TEST DONE] defaultAnalyticsGranted matches inverse of debug mode")
    }
}
