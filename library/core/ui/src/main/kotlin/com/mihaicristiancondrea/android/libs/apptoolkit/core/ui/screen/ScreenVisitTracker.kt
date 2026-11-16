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

import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.AnalyticsReporter
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.TrackedScreen
import java.util.concurrent.atomic.AtomicLong

/**
 * When one screen's visits start and end, and what it reports for them. Called on the main thread
 * only, from [ScreenHost]'s lifecycle observer and effects.
 */
internal class ScreenVisitTracker(private val screen: TrackedScreen) {

    /** Whether a visit has started and not yet ended. */
    var inVisit: Boolean = false
        private set

    /**
     * Which entrance the screen is on: it changes once the screen leaves composition by navigation,
     * so returning plays the entrance again, and stays through the background and a configuration
     * change. Unique across processes, since elements save the one they played in.
     */
    var entranceId: Long = nextEntranceId()
        private set

    /**
     * Whether this is the first copy of [screen] opened while the app runs. A tracker lives as long
     * as its destination, so going back to the screen keeps it and opening the screen anew does not.
     */
    val firstInRun: Boolean = OpenedScreens.claim(screen)

    /** The [LocalScreenOpening] the screen was last opened with, null before its first entrance. */
    private var lastOpening: Int? = null

    /** The entrance the screen was last opened in. */
    private var openedIn: Long = entranceId

    /**
     * The entrance the screen was last opened in, [entranceId] once [opening] differs from the one
     * it was last opened with: a tab picked again opens its screen, going back to it does not.
     */
    fun openedAt(opening: Int): Long {
        if (opening != lastOpening) {
            lastOpening = opening
            openedIn = entranceId
        }
        return openedIn
    }

    /** The status label last reported in this visit. */
    private var reportedLabel: String? = null

    /** The screen is visible and active; starts a visit unless one is going on. */
    fun onResumed(label: String?, telemetry: AnalyticsReporter) {
        if (!inVisit) {
            inVisit = true
            reportedLabel = null
            telemetry.logScreenView(screenName = screen.name, screenClass = screen.className)
        }
        reportStatus(label, telemetry)
    }

    /** The status changed. Reported only during a visit; a visit's start reports its own. */
    fun onStatus(label: String?, telemetry: AnalyticsReporter) {
        if (inVisit) reportStatus(label, telemetry)
    }

    /** The screen left composition or the app stopped; a configuration change keeps the visit. */
    fun onLeft(changingConfigurations: Boolean) {
        if (!changingConfigurations) inVisit = false
    }

    /** The screen left composition; unless only its configuration changed, it enters anew. */
    fun onDisposed(changingConfigurations: Boolean) {
        if (!changingConfigurations) entranceId = nextEntranceId()
    }

    private fun reportStatus(label: String?, telemetry: AnalyticsReporter) {
        if (label == null || label == reportedLabel) return
        reportedLabel = label
        telemetry.logEvent(
            AnalyticsEvent(
                name = SCREEN_STATE_EVENT,
                params = mapOf(
                    SCREEN_PARAM to AnalyticsValue.Str(screen.name),
                    STATE_PARAM to AnalyticsValue.Str(label),
                ),
            ),
        )
    }

    private companion object {
        // Dashboards read these names; they are the ones the screen_state event has always used.
        const val SCREEN_STATE_EVENT: String = "screen_state"
        const val SCREEN_PARAM: String = "screen"
        const val STATE_PARAM: String = "state"
    }
}

/** Seeded from the clock, so an id saved before process death never matches one issued after. */
private val entranceIds = AtomicLong(System.nanoTime())

private fun nextEntranceId(): Long = entranceIds.incrementAndGet()
