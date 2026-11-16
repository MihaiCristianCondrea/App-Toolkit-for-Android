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
 * Learns from the app's own analytics, on the device: sees every event and screen view the app and
 * the Toolkit report, so the app can adapt to what the person does with it, such as ordering a
 * dashboard by the tools they actually use.
 *
 * It sees them whatever the analytics consent, because nothing it receives leaves the device: an
 * observer that sends anything on must not. Bind one or more in Koin
 * (`single { MyObserver(...) } bind AnalyticsObserver::class`); `firebaseModule` hands every event
 * to all of them after it reports it. Each call runs on the caller's thread, so an observer does
 * its work on its own scope and returns at once. One that throws is skipped, never the event.
 */
interface AnalyticsObserver {

    /** An event the app reported. */
    fun onEvent(event: AnalyticsEvent) = Unit

    /** A screen view the app reported, by its stable screen name. */
    fun onScreenView(screenName: String, screenClass: String?) = Unit
}
