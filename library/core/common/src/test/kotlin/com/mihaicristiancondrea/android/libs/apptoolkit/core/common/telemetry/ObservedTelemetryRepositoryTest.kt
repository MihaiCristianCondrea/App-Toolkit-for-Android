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

package com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.testing.FakeTelemetryRepository
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ObservedTelemetryRepositoryTest {

    private class RecordingObserver : AnalyticsObserver {
        val events = mutableListOf<String>()
        val screens = mutableListOf<String>()
        override fun onEvent(event: AnalyticsEvent) { events += event.name }
        override fun onScreenView(screenName: String, screenClass: String?) { screens += screenName }
    }

    @Test
    fun `events and screen views are reported, then reach every observer`() {
        val delegate = FakeTelemetryRepository()
        val observer = RecordingObserver()
        val repository = ObservedTelemetryRepository(delegate = delegate, observers = { listOf(observer) })

        repository.logEvent(AnalyticsEvent(name = "cleanup_done"))
        repository.logScreenView("Duplicates", "DuplicatesScreen")

        assertEquals(listOf("cleanup_done"), delegate.loggedEvents.map { it.name })
        assertEquals(listOf("cleanup_done"), observer.events)
        assertEquals(listOf("Duplicates"), observer.screens)
    }

    @Test
    fun `an observer that throws skips itself, not the event or the observers after it`() {
        val delegate = FakeTelemetryRepository()
        val after = RecordingObserver()
        val failing = object : AnalyticsObserver {
            override fun onEvent(event: AnalyticsEvent) = error("broken")
        }
        val repository = ObservedTelemetryRepository(delegate = delegate, observers = { listOf(failing, after) })

        repository.logEvent(AnalyticsEvent(name = "share"))

        assertEquals(1, delegate.loggedEvents.size)
        assertEquals(listOf("share"), after.events)
    }
}
