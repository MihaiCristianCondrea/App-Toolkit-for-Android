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

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsEvent
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.domain.models.analytics.AnalyticsValue
import com.mihaicristiancondrea.android.libs.apptoolkit.core.ui.views.analytics.LocalTelemetry

/**
 * Reports `screen_state` with [state]'s [TrackedStatus.trackingLabel] each time the label changes,
 * the same event the previous `TrackScreenState` sent for a whole screen's state.
 */
@Composable
fun TrackScreenState(
    screenName: String,
    state: TrackedStatus,
) {
    val telemetryRepository = LocalTelemetry.current
    val label: String = state.trackingLabel
    LaunchedEffect(screenName, label) {
        telemetryRepository.logEvent(
            AnalyticsEvent(
                name = "screen_state",
                params = mapOf(
                    "screen" to AnalyticsValue.Str(screenName),
                    "state" to AnalyticsValue.Str(label),
                ),
            ),
        )
    }
}
