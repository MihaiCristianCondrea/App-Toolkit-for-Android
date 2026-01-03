package com.d4rk.android.libs.apptoolkit.core.utils.platform

import com.d4rk.android.libs.apptoolkit.app.settings.utils.providers.BuildInfoProvider
import com.d4rk.android.libs.apptoolkit.core.domain.repository.FirebaseController
import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import io.mockk.every
import io.mockk.mockk
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
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        startKoin {
            modules(
                module {
                    single<FirebaseController> { firebaseController }
                    single<BuildInfoProvider> { mockk(relaxed = true) }
                }
            )
        }

        ConsentManagerHelper.updateConsent(
            analyticsGranted = true,
            adStorageGranted = false,
            adUserDataGranted = true,
            adPersonalizationGranted = false
        )

        verify {
            firebaseController.updateConsent(
                analyticsGranted = true,
                adStorageGranted = false,
                adUserDataGranted = true,
                adPersonalizationGranted = false
            )
        }
        stopKoin()
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
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        startKoin {
            modules(
                module {
                    single<BuildInfoProvider> { provider }
                    single<FirebaseController> { firebaseController }
                }
            )
        }

        ConsentManagerHelper.applyInitialConsent(dataStore)

        verify { firebaseController.updateConsent(any(), any(), any(), any()) }
        verify { firebaseController.setAnalyticsEnabled(true) }
        verify { firebaseController.setCrashlyticsEnabled(true) }
        verify { firebaseController.setPerformanceEnabled(true) }

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

        val firebaseController = mockk<FirebaseController>(relaxed = true)
        startKoin {
            modules(
                module {
                    single<FirebaseController> { firebaseController }
                    single<BuildInfoProvider> { mockk(relaxed = true) }
                }
            )
        }

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        verify { firebaseController.setAnalyticsEnabled(true) }
        verify { firebaseController.setCrashlyticsEnabled(true) }
        verify { firebaseController.setPerformanceEnabled(true) }

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        verify { firebaseController.setAnalyticsEnabled(false) }
        verify { firebaseController.setCrashlyticsEnabled(false) }
        verify { firebaseController.setPerformanceEnabled(false) }
        stopKoin()
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

        val firebaseController = mockk<FirebaseController>(relaxed = true)
        every { firebaseController.setCrashlyticsEnabled(any()) } throws RuntimeException("fail")
        startKoin {
            modules(
                module {
                    single<FirebaseController> { firebaseController }
                    single<BuildInfoProvider> { mockk(relaxed = true) }
                }
            )
        }

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
        val firebaseController = mockk<FirebaseController>(relaxed = true)
        startKoin {
            modules(
                module {
                    single<BuildInfoProvider> { provider }
                    single<FirebaseController> { firebaseController }
                }
            )
        }

        val field =
            ConsentManagerHelper::class.java.getDeclaredField($$"defaultAnalyticsGranted$delegate")
        field.isAccessible = true
        field.set(ConsentManagerHelper, lazy { !provider.isDebugBuild })

        ConsentManagerHelper.applyInitialConsent(dataStore)

        verify { firebaseController.updateConsent(any(), any(), any(), any()) }
        verify { firebaseController.setAnalyticsEnabled(false) }
        verify { firebaseController.setCrashlyticsEnabled(false) }
        verify { firebaseController.setPerformanceEnabled(false) }

        stopKoin()
        println("🏁 [TEST DONE] applyInitialConsent when debug build uses datastore values")
    }

    @Test
    fun `updateConsent propagates firebase exception`() {
        println("🚀 [TEST] updateConsent propagates firebase exception")
        val firebaseController = mockk<FirebaseController>()
        every {
            firebaseController.updateConsent(
                analyticsGranted = true,
                adStorageGranted = true,
                adUserDataGranted = true,
                adPersonalizationGranted = true
            )
        } throws RuntimeException("fail")
        startKoin {
            modules(
                module {
                    single<FirebaseController> { firebaseController }
                    single<BuildInfoProvider> { mockk(relaxed = true) }
                }
            )
        }

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

        val firebaseController = mockk<FirebaseController>(relaxed = true)
        startKoin {
            modules(
                module {
                    single<FirebaseController> { firebaseController }
                    single<BuildInfoProvider> { mockk(relaxed = true) }
                }
            )
        }

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        verify { firebaseController.setAnalyticsEnabled(false) }
        verify { firebaseController.setCrashlyticsEnabled(false) }
        verify { firebaseController.setPerformanceEnabled(false) }

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        verify { firebaseController.setAnalyticsEnabled(true) }
        verify { firebaseController.setCrashlyticsEnabled(true) }
        verify { firebaseController.setPerformanceEnabled(true) }
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
