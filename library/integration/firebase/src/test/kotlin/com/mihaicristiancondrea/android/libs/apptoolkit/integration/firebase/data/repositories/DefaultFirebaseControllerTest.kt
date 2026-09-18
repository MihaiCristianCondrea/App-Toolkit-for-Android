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

package com.mihaicristiancondrea.android.libs.apptoolkit.integration.firebase.data.repositories

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.slot
import io.mockk.unmockkAll
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DefaultFirebaseControllerTest {

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var crashlytics: FirebaseCrashlytics
    private lateinit var performance: FirebasePerformance
    private lateinit var controller: DefaultFirebaseController

    @BeforeEach
    fun setUp() {
        mockkConstructor(Bundle::class)
        every { anyConstructed<Bundle>().putString(any(), any()) } returns Unit
        every { anyConstructed<Bundle>().putLong(any(), any()) } returns Unit
        every { anyConstructed<Bundle>().putDouble(any(), any()) } returns Unit

        analytics = mockk(relaxed = true)
        crashlytics = mockk(relaxed = true)
        performance = mockk(relaxed = true)
        controller = DefaultFirebaseController(
            analyticsProvider = { analytics },
            crashlyticsProvider = { crashlytics },
            performanceProvider = { performance },
        )
    }

    @AfterEach
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `setAnalyticsEnabled updates analytics collection state`() {
        controller.setAnalyticsEnabled(true)
        verify { analytics.setAnalyticsCollectionEnabled(true) }

        controller.setAnalyticsEnabled(false)
        verify { analytics.setAnalyticsCollectionEnabled(false) }
    }

    @Test
    fun `setCrashlyticsEnabled updates crashlytics collection state`() {
        controller.setCrashlyticsEnabled(true)
        verify { crashlytics.isCrashlyticsCollectionEnabled = true }

        controller.setCrashlyticsEnabled(false)
        verify { crashlytics.isCrashlyticsCollectionEnabled = false }
    }

    @Test
    fun `setPerformanceEnabled updates performance collection state`() {
        controller.setPerformanceEnabled(true)
        verify { performance.isPerformanceCollectionEnabled = true }

        controller.setPerformanceEnabled(false)
        verify { performance.isPerformanceCollectionEnabled = false }
    }

    @Test
    fun `updateConsent maps and sets consent values`() {
        val slot = slot<Map<FirebaseAnalytics.ConsentType, FirebaseAnalytics.ConsentStatus>>()
        every { analytics.setConsent(capture(slot)) } returns Unit

        controller.updateConsent(
            analyticsGranted = true,
            adStorageGranted = false,
            adUserDataGranted = true,
            adPersonalizationGranted = false,
        )

        val consent = slot.captured
        assertEquals(FirebaseAnalytics.ConsentStatus.GRANTED, consent[FirebaseAnalytics.ConsentType.ANALYTICS_STORAGE])
        assertEquals(FirebaseAnalytics.ConsentStatus.DENIED, consent[FirebaseAnalytics.ConsentType.AD_STORAGE])
        assertEquals(FirebaseAnalytics.ConsentStatus.GRANTED, consent[FirebaseAnalytics.ConsentType.AD_USER_DATA])
        assertEquals(FirebaseAnalytics.ConsentStatus.DENIED, consent[FirebaseAnalytics.ConsentType.AD_PERSONALIZATION])
    }

    @Test
    fun `logEvent delivers valid analytics event and parameters`() {
        val eventSlot = slot<String>()
        val bundleSlot = slot<Bundle>()
        every { analytics.logEvent(capture(eventSlot), capture(bundleSlot)) } returns Unit

        val event = AnalyticsEvent(
            name = "custom_event",
            params = mapOf(
                "str_param" to AnalyticsValue.Str("hello"),
                "long_param" to AnalyticsValue.LongVal(42L),
                "double_param" to AnalyticsValue.DoubleVal(3.14),
                "bool_param" to AnalyticsValue.Bool(true),
            ),
        )

        controller.logEvent(event)

        assertEquals("custom_event", eventSlot.captured)
        verify { anyConstructed<Bundle>().putString("str_param", "hello") }
        verify { anyConstructed<Bundle>().putLong("long_param", 42L) }
        verify { anyConstructed<Bundle>().putDouble("double_param", 3.14) }
        verify { anyConstructed<Bundle>().putString("bool_param", "true") }
    }

    @Test
    fun `logEvent drops invalid event name and logs breadcrumb`() {
        val event = AnalyticsEvent(
            name = "firebase_reserved_event",
            params = emptyMap(),
        )

        controller.logEvent(event)

        verify(exactly = 0) { analytics.logEvent(any(), any()) }
        verify { crashlytics.log(match { it.contains("analytics_drop_invalid_event") }) }
    }

    @Test
    fun `logScreenView logs screen view event with parameters`() {
        val bundleSlot = slot<Bundle>()
        every { analytics.logEvent(eq(FirebaseAnalytics.Event.SCREEN_VIEW), capture(bundleSlot)) } returns Unit

        controller.logScreenView(screenName = "SettingsScreen", screenClass = "SettingsActivity")

        verify { anyConstructed<Bundle>().putString(FirebaseAnalytics.Param.SCREEN_NAME, "SettingsScreen") }
        verify { anyConstructed<Bundle>().putString(FirebaseAnalytics.Param.SCREEN_CLASS, "SettingsActivity") }
        verify { analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, any()) }
    }

    @Test
    fun `setUserProperty delegates valid property name and value`() {
        controller.setUserProperty("theme_mode", "dark")

        verify { analytics.setUserProperty("theme_mode", "dark") }
    }

    @Test
    fun `setUserProperty drops invalid property name`() {
        controller.setUserProperty("ga_reserved_property", "value")

        verify(exactly = 0) { analytics.setUserProperty("ga_reserved_property", any()) }
        verify { crashlytics.log(match { it.contains("analytics_drop_invalid_user_property") }) }
    }

    @Test
    fun `logBreadcrumb formats message and attributes`() {
        controller.logBreadcrumb(
            message = "user_clicked_button",
            attributes = mapOf("button_id" to "save", "screen" to "profile"),
        )

        verify { crashlytics.log("user_clicked_button | button_id=save, screen=profile") }
    }

    @Test
    fun `reportViewModelError sets metadata and records exception`() {
        val exception = RuntimeException("test error")

        controller.reportViewModelError(
            viewModelName = "MainViewModel",
            action = "fetchData",
            throwable = exception,
            extraKeys = mapOf("retry_count" to "3"),
        )

        verify { crashlytics.setCustomKey("view_model", "MainViewModel") }
        verify { crashlytics.setCustomKey("action", "fetchData") }
        verify { crashlytics.setCustomKey("retry_count", "3") }
        verify { crashlytics.recordException(exception) }
    }

    @Test
    fun `recordNonFatal sets metadata and records exception`() {
        val exception = IllegalStateException("non fatal")

        controller.recordNonFatal(
            throwable = exception,
            attributes = mapOf("feature" to "billing"),
        )

        verify { crashlytics.setCustomKey("feature", "billing") }
        verify { crashlytics.recordException(exception) }
    }
}
