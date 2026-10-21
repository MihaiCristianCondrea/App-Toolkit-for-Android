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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.screen

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ScreenVisitTrackerTest {

    private val screen = TrackedScreen(name = "News", className = "NewsScreen")
    private val telemetry = FakeTelemetryRepository()
    private val tracker = ScreenVisitTracker(screen)

    private fun views(): Int = telemetry.loggedScreenViews.size

    private fun states(): List<String> = telemetry.loggedEvents
        .filter { it.name == "screen_state" }
        .map { (it.params.getValue("state") as AnalyticsValue.Str).value }

    @Test
    fun `the first resume reports one visit with its status`() {
        tracker.onResumed("Loading", telemetry)

        assertEquals(listOf("News" to "NewsScreen"), telemetry.loggedScreenViews)
        assertEquals(listOf("Loading"), states())
        assertEquals(
            AnalyticsValue.Str("News"),
            telemetry.loggedEvents.single().params["screen"],
        )
    }

    @Test
    fun `resuming again within a visit reports nothing`() {
        tracker.onResumed("Ready", telemetry)
        // A cancelled back gesture or a dismissed overlay: paused, then resumed, never left.
        tracker.onResumed("Ready", telemetry)

        assertEquals(1, views())
        assertEquals(listOf("Ready"), states())
    }

    @Test
    fun `status changes are reported once each, repeats are not`() {
        tracker.onResumed("Loading", telemetry)
        tracker.onStatus("Loading", telemetry)
        tracker.onStatus("Ready", telemetry)
        tracker.onStatus("Ready", telemetry)
        tracker.onStatus("Failed", telemetry)

        assertEquals(listOf("Loading", "Ready", "Failed"), states())
    }

    @Test
    fun `a status before the visit starts waits for it`() {
        // Composed but not yet resumed: an entering transition.
        tracker.onStatus("Loading", telemetry)
        assertEquals(0, views())
        assertEquals(emptyList<String>(), states())

        tracker.onResumed("Ready", telemetry)

        assertEquals(1, views())
        assertEquals(listOf("Ready"), states())
    }

    @Test
    fun `leaving ends the visit and the return is a new one`() {
        tracker.onResumed("Ready", telemetry)
        tracker.onLeft(changingConfigurations = false)
        tracker.onStatus("Ready", telemetry)
        tracker.onResumed("Ready", telemetry)

        assertEquals(2, views())
        assertEquals(listOf("Ready", "Ready"), states())
    }

    @Test
    fun `a configuration change keeps the visit`() {
        tracker.onResumed("Ready", telemetry)
        tracker.onLeft(changingConfigurations = true)
        tracker.onResumed("Ready", telemetry)

        assertEquals(1, views())
        assertEquals(listOf("Ready"), states())
    }

    @Test
    fun `after process death the return is a new visit`() {
        tracker.onResumed("Ready", telemetry)
        // The tracker lives in the destination's ViewModelStore, which process death clears.
        ScreenVisitTracker(screen).onResumed("Ready", telemetry)

        assertEquals(2, views())
    }

    @Test
    fun `a screen without a status reports only its visits`() {
        tracker.onResumed(null, telemetry)
        tracker.onStatus(null, telemetry)

        assertEquals(1, views())
        assertEquals(emptyList<String>(), states())
    }

    @Test
    fun `one destination keeps one tracker per screen`() {
        val visits = ScreenVisits()
        val list = TrackedScreen(name = "List")

        assertEquals(true, visits.trackerFor(list) === visits.trackerFor(TrackedScreen(name = "List")))
        assertEquals(false, visits.trackerFor(list) === visits.trackerFor(screen))
    }

    @Test
    fun `leaving by navigation starts a new entrance`() {
        val first = tracker.entranceId

        tracker.onLeft(changingConfigurations = false)
        tracker.onDisposed(changingConfigurations = false)

        assertEquals(false, tracker.entranceId == first)
    }

    @Test
    fun `a configuration change and the background keep the entrance`() {
        val first = tracker.entranceId

        tracker.onLeft(changingConfigurations = true)
        tracker.onDisposed(changingConfigurations = true)
        tracker.onLeft(changingConfigurations = false)

        assertEquals(first, tracker.entranceId)
    }
}
