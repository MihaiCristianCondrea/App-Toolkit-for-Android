package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.ads

import android.util.Log
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.TelemetryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AdLoadReporterTest {
    private val telemetry = mockk<TelemetryRepository>(relaxed = true)
    private val reporter = AdLoadReporter(telemetry, mockk(relaxed = true))

    @BeforeEach
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.w(any(), any<String>()) } returns 0
        every { Log.w(any(), any<String>(), any()) } returns 0
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `network failures and no fill retain breadcrumbs without nonfatals`() {
        listOf("NETWORK_ERROR", "NO_FILL").forEach { code ->
            reporter.onAdFailedToLoad("catalog", "unit", code, "unavailable")
            verify { telemetry.logBreadcrumb("Ad slot failed to load", match { it["error_code"] == code }) }
        }
        verify(exactly = 0) { telemetry.recordNonFatal(any(), any()) }
    }

    @Test
    fun `unexpected SDK errors remain nonfatal reports`() {
        reporter.onAdFailedToLoad("catalog", "unit", "INVALID_REQUEST", "invalid unit")
        verify(exactly = 1) { telemetry.recordNonFatal(ofType<AdSlotLoadException>(), any()) }
    }

    @Test
    fun `a loader that cannot start remains a nonfatal report`() {
        reporter.onAdRequestNotStarted("catalog", "unit")
        verify(exactly = 1) { telemetry.recordNonFatal(ofType<AdSlotNotRequestedException>(), any()) }
    }
}
