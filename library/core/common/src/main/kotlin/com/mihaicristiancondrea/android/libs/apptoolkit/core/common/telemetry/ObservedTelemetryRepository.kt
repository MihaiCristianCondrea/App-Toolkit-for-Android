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

/**
 * [delegate], whose every event and screen view also reaches the app's [AnalyticsObserver]s once it
 * has reported it. [observers] is read the first time an event arrives, so an observer bound in a
 * module loaded later is still found.
 */
class ObservedTelemetryRepository(
    private val delegate: TelemetryRepository,
    observers: () -> List<AnalyticsObserver>,
) : TelemetryRepository by delegate {

    private val observers: List<AnalyticsObserver> by lazy(observers)

    override fun logEvent(event: AnalyticsEvent) {
        delegate.logEvent(event)
        observers.forEach { observer -> runCatching { observer.onEvent(event) } }
    }

    override fun logScreenView(screenName: String, screenClass: String?) {
        delegate.logScreenView(screenName, screenClass)
        observers.forEach { observer -> runCatching { observer.onScreenView(screenName, screenClass) } }
    }
}
