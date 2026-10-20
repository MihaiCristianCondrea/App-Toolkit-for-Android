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

import androidx.compose.runtime.Stable
import com.mihaicristiancondrea.android.libs.apptoolkit.core.common.telemetry.models.AnalyticsEvent

/**
 * Product analytics: what people do in the app, as GA4 events, screen views and user properties.
 *
 * Analytics belong to the UI layer, where the action happens: a composable reports through
 * `LocalTelemetry` and a ViewModel through its injected [TelemetryRepository]. Code under `data/`
 * and `domain/` never reports analytics: a repository returns its outcome, such as whether an unlock
 * was saved, and the screen that asked reports it. `checkModuleBoundaries` enforces that rule.
 */
@Stable
interface AnalyticsReporter {

    fun logEvent(event: AnalyticsEvent)

    fun logScreenView(screenName: String, screenClass: String? = null)

    fun setUserProperty(name: String, value: String?)
}
