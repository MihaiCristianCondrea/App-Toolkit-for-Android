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
import com.google.firebase.crashlytics.CustomKeysAndValues
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.perf.FirebasePerformance
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
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

class FirebaseTelemetryRepositoryTest {

    private lateinit var analytics: FirebaseAnalytics
    private lateinit var crashlytics: FirebaseCrashlytics
    private lateinit var performance: FirebasePerformance
    private lateinit var controller: FirebaseTelemetryRepository

    @BeforeEach
    fun setUp() {
        mockkConstructor(Bundle::class)
        every { anyConstructed<Bundle>().putString(any(), any()) } returns Unit
        every { anyConstructed<Bundle>().putLong(any(), any()) } returns Unit
        every { anyConstructed<Bundle>().putDouble(any(), any()) } returns Unit

        analytics = mockk(relaxed = true)
        crashlytics = mockk(relaxed = true)
        performance = mockk(relaxed = true)
        controller = FirebaseTelemetryRepository(
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
    fun `logEvent caps parameters at 25, truncates strings at 100 chars, and drops invalid or non-finite params`() {
        val params = buildMap {
            put("firebase_reserved", AnalyticsValue.Str("blocked"))
            put("1invalid", AnalyticsValue.Str("blocked"))
            put("nan_param", AnalyticsValue.DoubleVal(Double.NaN))
            put("inf_param", AnalyticsValue.DoubleVal(Double.POSITIVE_INFINITY))
            put("long_str", AnalyticsValue.Str("a".repeat(150)))
            repeat(30) { index ->
                put("p_$index", AnalyticsValue.LongVal(index.toLong()))
            }
        }

        controller.logEvent(AnalyticsEvent(name = "capped_event", params = params))

        verify { anyConstructed<Bundle>().putString("long_str", "a".repeat(100)) }
        verify(exactly = 0) { anyConstructed<Bundle>().putString("firebase_reserved", any()) }
        verify(exactly = 0) { anyConstructed<Bundle>().putString("1invalid", any()) }
        verify(exactly = 0) { anyConstructed<Bundle>().putDouble("nan_param", any()) }
        verify(exactly = 0) { anyConstructed<Bundle>().putDouble("inf_param", any()) }
        verify(exactly = 24) { anyConstructed<Bundle>().putLong(any(), any()) }
        verify { crashlytics.log(match { it.contains("analytics_drop_invalid_param") }) }
        verify { crashlytics.log(match { it.contains("analytics_drop_non_finite_param") }) }
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
    fun `logScreenView drops blank screen names and omits blank screen class`() {
        controller.logScreenView(screenName = "   ", screenClass = "MainActivity")
        verify(exactly = 0) { analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, any()) }
        verify { crashlytics.log("analytics_drop_invalid_screen_view") }

        controller.logScreenView(screenName = "Home", screenClass = "  ")
        verify { anyConstructed<Bundle>().putString(FirebaseAnalytics.Param.SCREEN_NAME, "Home") }
        verify(exactly = 0) { anyConstructed<Bundle>().putString(FirebaseAnalytics.Param.SCREEN_CLASS, any()) }
    }

    @Test
    fun `setUserProperty delegates valid property name and truncates value to 36 chars or clears null`() {
        controller.setUserProperty("theme_mode", "x".repeat(50))
        verify { analytics.setUserProperty("theme_mode", "x".repeat(36)) }

        controller.setUserProperty("theme_mode", null)
        verify { analytics.setUserProperty("theme_mode", null) }
    }

    @Test
    fun `setUserProperty drops reserved or overlong property names instead of truncating`() {
        controller.setUserProperty("ga_reserved_property", "value")
        controller.setUserProperty("property_name_exceeding_24_chars", "value")

        verify(exactly = 0) { analytics.setUserProperty(any(), any()) }
        verify(exactly = 2) { crashlytics.log(match { it.contains("analytics_drop_invalid_user_property") }) }
    }

    @Test
    fun `logBreadcrumb formats message with and without attributes`() {
        controller.logBreadcrumb(message = "plain_event")
        verify { crashlytics.log("plain_event") }

        controller.logBreadcrumb(
            message = "user_clicked_button",
            attributes = mapOf("button_id" to "save", "screen" to "profile"),
        )
        verify { crashlytics.log("user_clicked_button | button_id=save, screen=profile") }
    }

    @Test
    fun `reportViewModelError preserves canonical keys and records exception without persistent keys`() {
        val exception = RuntimeException("test error")
        val recordedKeys = mutableMapOf<String, String>()
        mockkConstructor(CustomKeysAndValues.Builder::class)
        every {
            anyConstructed<CustomKeysAndValues.Builder>().putString(any(), any())
        } answers {
            recordedKeys[firstArg()] = secondArg()
            self as CustomKeysAndValues.Builder
        }

        controller.reportViewModelError(
            viewModelName = "MainViewModel",
            action = "fetchData",
            throwable = exception,
            extraKeys = mapOf("retry_count" to "3", "view_model" to "spoofed", "action" to "spoofed"),
        )

        verify { crashlytics.log("ViewModel catch in MainViewModel during fetchData") }
        verify { crashlytics.recordException(exception, any<CustomKeysAndValues>()) }
        assertEquals("MainViewModel", recordedKeys["view_model"])
        assertEquals("fetchData", recordedKeys["action"])
        assertEquals("3", recordedKeys["retry_count"])
        assertEquals(RuntimeException::class.java.name, recordedKeys["exception_type"])
        assertEquals("test error", recordedKeys["exception_message"])
        // The keys belong to this report only; persistent keys would follow every later crash.
        verify(exactly = 0) { crashlytics.setCustomKey(any<String>(), any<String>()) }
    }

    @Test
    fun `recordNonFatal sets metadata and records exception`() {
        val exception = IllegalStateException("non fatal")

        controller.recordNonFatal(
            throwable = exception,
            attributes = mapOf("feature" to "billing"),
        )

        verify { crashlytics.recordException(exception, any<CustomKeysAndValues>()) }
        verify(exactly = 0) { crashlytics.setCustomKey(any<String>(), any<String>()) }
    }

    @Test
    fun `uninitialized FirebaseApp providers fail closed without throwing`() {
        val uninitialized = FirebaseTelemetryRepository(
            analyticsProvider = { error("Default FirebaseApp is not initialized") },
            crashlyticsProvider = { error("Default FirebaseApp is not initialized") },
            performanceProvider = { error("Default FirebaseApp is not initialized") },
        )

        uninitialized.updateConsent(
            analyticsGranted = true,
            adStorageGranted = true,
            adUserDataGranted = false,
            adPersonalizationGranted = false,
        )
        uninitialized.setAnalyticsEnabled(true)
        uninitialized.setCrashlyticsEnabled(true)
        uninitialized.setPerformanceEnabled(true)
        uninitialized.logBreadcrumb("msg")
        uninitialized.logEvent(AnalyticsEvent(name = "custom_event"))
        uninitialized.logScreenView(screenName = "Home", screenClass = null)
        uninitialized.setUserProperty("theme_mode", "dark")
        uninitialized.reportViewModelError("MainViewModel", "load", IllegalStateException("boom"))
        uninitialized.recordNonFatal(IllegalStateException("boom"))
    }
}
