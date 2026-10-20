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

/**
 * The app's whole telemetry in one type: [AnalyticsReporter], [DiagnosticsReporter] and
 * [TelemetryCollection].
 *
 * The analytics and crash SDKs are its data sources, so callers reach them through this contract
 * and never depend on a vendor. The implementation is the only place that touches an SDK;
 * `:library:integration:firebase` binds `FirebaseTelemetryRepository`, and `telemetryModule` binds
 * the three narrower contracts to the same instance.
 *
 * It is what the UI layer uses: ViewModels take it, `LoggedScreenViewModel` reports through it, and
 * composables read it as `val telemetry = LocalTelemetry.current`. A screen view or a tap is a UI
 * event, and sending it through a ViewModel would add an event per tap and change nothing about
 * what is reported. Code under `data/` and `domain/` takes [DiagnosticsReporter], and
 * [TelemetryCollection] where it applies consent, instead.
 */
@Stable
interface TelemetryRepository : AnalyticsReporter, DiagnosticsReporter, TelemetryCollection
