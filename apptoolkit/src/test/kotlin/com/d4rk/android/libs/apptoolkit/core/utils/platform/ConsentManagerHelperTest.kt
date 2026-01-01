package com.d4rk.android.libs.apptoolkit.core.utils.platform

import com.d4rk.android.libs.apptoolkit.data.local.datastore.CommonDataStore
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import kotlin.test.assertEquals

class ConsentManagerHelperTest {

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `updateConsent sets consent statuses for every boolean combination`() {
        val analytics = mockk<FirebaseAnalytics>(relaxed = true)
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics

        val capturedConsents =
            mutableListOf<Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus>>()
        every { analytics.setConsent(any()) } answers {
            @Suppress("UNCHECKED_CAST")
            capturedConsents += firstArg<Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus>>()
        }

        val combinations = mutableListOf<ConsentFlags>()
        listOf(false, true).forEach { analyticsGranted ->
            listOf(false, true).forEach { adStorageGranted ->
                listOf(false, true).forEach { adUserDataGranted ->
                    listOf(false, true).forEach { adPersonalizationGranted ->
                        combinations += ConsentFlags(
                            analyticsGranted = analyticsGranted,
                            adStorageGranted = adStorageGranted,
                            adUserDataGranted = adUserDataGranted,
                            adPersonalizationGranted = adPersonalizationGranted
                        )
                        ConsentManagerHelper.updateConsent(
                            analyticsGranted = analyticsGranted,
                            adStorageGranted = adStorageGranted,
                            adUserDataGranted = adUserDataGranted,
                            adPersonalizationGranted = adPersonalizationGranted
                        )
                    }
                }
            }
        }

        assertEquals(combinations.size, capturedConsents.size)
        combinations.forEachIndexed { index, flags ->
            val consent = capturedConsents[index]
            assertEquals(
                flags.analyticsGranted.toStatus(),
                consent[FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE],
                "ANALYTICS_STORAGE consent mismatch for $flags"
            )
            assertEquals(
                flags.adStorageGranted.toStatus(),
                consent[FirebaseAnalytics.ConsentType.AD_STORAGE],
                "AD_STORAGE consent mismatch for $flags"
            )
            assertEquals(
                flags.adUserDataGranted.toStatus(),
                consent[FirebaseAnalytics.ConsentType.AD_USER_DATA],
                "AD_USER_DATA consent mismatch for $flags"
            )
            assertEquals(
                flags.adPersonalizationGranted.toStatus(),
                consent[FirebaseAnalytics.ConsentType.AD_PERSONALIZATION],
                "AD_PERSONALIZATION consent mismatch for $flags"
            )
        }

        verify(exactly = combinations.size) { analytics.setConsent(any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `applyInitialConsent pulls flags then delegates to updateConsent and updateAnalyticsCollection`() =
        runTest {
            val dataStore = mockk<CommonDataStore>()
            val defaultValue = true
            every { dataStore.analyticsConsent(defaultValue) } returns flowOf(true)
            every { dataStore.adStorageConsent(defaultValue) } returns flowOf(false)
            every { dataStore.adUserDataConsent(defaultValue) } returns flowOf(true)
            every { dataStore.adPersonalizationConsent(defaultValue) } returns flowOf(false)

            mockkObject(ConsentManagerHelper)
            every { ConsentManagerHelper.defaultAnalyticsGranted } returns defaultValue
            coEvery { ConsentManagerHelper.applyInitialConsent(any()) } coAnswers { callOriginal() }
            every { ConsentManagerHelper.updateConsent(any(), any(), any(), any()) } answers { }
            coEvery { ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(any()) } returns Unit

            ConsentManagerHelper.applyInitialConsent(dataStore)

            verify(exactly = 1) { dataStore.analyticsConsent(defaultValue) }
            verify(exactly = 1) { dataStore.adStorageConsent(defaultValue) }
            verify(exactly = 1) { dataStore.adUserDataConsent(defaultValue) }
            verify(exactly = 1) { dataStore.adPersonalizationConsent(defaultValue) }

            verify(exactly = 1) {
                ConsentManagerHelper.updateConsent(
                    analyticsGranted = true,
                    adStorageGranted = false,
                    adUserDataGranted = true,
                    adPersonalizationGranted = false
                )
            }

            coVerify(exactly = 1) {
                ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(
                    dataStore
                )
            }
            coVerifyOrder {
                ConsentManagerHelper.updateConsent(
                    analyticsGranted = true,
                    adStorageGranted = false,
                    adUserDataGranted = true,
                    adPersonalizationGranted = false
                )
                ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateAnalyticsCollectionFromDatastore toggles all Firebase SDKs`() = runTest {
        val dataStore = mockk<CommonDataStore>()
        val defaultValue = false

        mockkObject(ConsentManagerHelper)
        every { ConsentManagerHelper.defaultAnalyticsGranted } returns defaultValue
        coEvery { ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(any()) } coAnswers { callOriginal() }

        every { dataStore.usageAndDiagnostics(defaultValue) } returnsMany listOf(
            flowOf(true),
            flowOf(false)
        )

        val analytics = mockk<FirebaseAnalytics>(relaxed = true)
        val crashlytics = mockk<FirebaseCrashlytics>(relaxed = true)
        val performance = mockk<FirebasePerformance>(relaxed = true)
        mockkObject(Firebase)
        every { Firebase.analytics } returns analytics
        mockkStatic(FirebaseCrashlytics::class)
        mockkStatic(FirebasePerformance::class)
        every { FirebaseCrashlytics.getInstance() } returns crashlytics
        every { FirebasePerformance.getInstance() } returns performance

        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)
        ConsentManagerHelper.updateAnalyticsCollectionFromDatastore(dataStore)

        verify(exactly = 2) { dataStore.usageAndDiagnostics(defaultValue) }
        verifyOrder {
            analytics.setAnalyticsCollectionEnabled(true)
            analytics.setAnalyticsCollectionEnabled(false)
        }
        verifyOrder {
            crashlytics.isCrashlyticsCollectionEnabled = true
            crashlytics.isCrashlyticsCollectionEnabled = false
        }
        verifyOrder {
            performance.isPerformanceCollectionEnabled = true
            performance.isPerformanceCollectionEnabled = false
        }
    }

    private data class ConsentFlags(
        val analyticsGranted: Boolean,
        val adStorageGranted: Boolean,
        val adUserDataGranted: Boolean,
        val adPersonalizationGranted: Boolean
    )

    private fun Boolean.toStatus(): FirebaseAnalytics.ConsentStatus =
        if (this) FirebaseAnalytics.ConsentStatus.GRANTED else FirebaseAnalytics.ConsentStatus.DENIED
}
